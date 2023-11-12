package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.*;
import com.bsideTinkerbell.wavingBe.domain.entity.*;
import com.bsideTinkerbell.wavingBe.repository.*;
import com.bsideTinkerbell.wavingBe.security.JwtService;
import com.bsideTinkerbell.wavingBe.util.EndpointNaver;
import com.google.gson.Gson;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.utils.Base64;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final FavoriteGreetingRepository favoriteGreetingRepository;
    private final LoginRepository loginRepository;
    private final PersonalAuthenticationRepository personalAuthenticationRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final ContactRepository contactRepository;
    private final FriendProfileRepository friendProfileRepository;

    private String makeSignature(
            String method
            , String timestamp
            , String accessKey
            , String secretKey
            , String url
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";					// one space
        String newLine = "\n";				// new line
        String message = method + space + url + newLine + timestamp + newLine + accessKey;

        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(rawHmac);
    }

    /**
     * 10000 ~ 99999 사이 인증 코드 생성
     * @return verificationCode
     */
    private int generateVerificationCode() {
        return new Random().nextInt(90000) + 10000;
    }

    /**
     *
     * @param password 암호화 안된 비밀번호
     * @return SHA512 암호화 된 비밀번호
     * @throws NoSuchAlgorithmException 알고리즘 예외처리
     */
    private String encryptPasswordWithSHA256(String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        return new String(hashedPassword);
    }

    private UserEntity getUserInfo(Long id) {
        return userRepository.findById(id).orElseThrow();
    }

    /**
     * 마이페이지 회원정보 본인인증에서 가져오기
     * @param id
     * @return 본인인증에 있는 회원 정보
     */
    private PersonalAuthenticationEntity getPersonalAuthInfo(Long id) {
        return personalAuthenticationRepository.findByUserId(id);
    }

    private void saveFavoriteGreeting(Long userId, UserFavoriteGreetingRequestDto request) {
        favoriteGreetingRepository.save(request.toEntity(userId));
    }

    private String formatCellphone(String cellphone) {
        return cellphone.replace("-", "");
    }

    /**
     * nCloud SENS 통해 휴대폰 인증번호 전송
     * @param personalAuthRequestDto 본인인증 데이터 전송 객체
     */
    public ResponseDto sendAuthenticationCode(PersonalAuthenticationRequestDto personalAuthRequestDto) throws NoSuchAlgorithmException, InvalidKeyException {
        String method = "POST";                                         // method
        String timestamp = String.valueOf(System.currentTimeMillis());	// current timestamp (epoch)
        String accessKey = "Ims2TbuxYF9fPCg9G2n6";			            // access key id (from portal or Sub Account)
        String secretKey = "8VwKPaIodjIu8YP1cTsW8KIKqkVJfaSBOBU7UwJF";  // secret key
        String baseUri = "https://sens.apigw.ntruss.com";               // sens base uri
        String apiUri = EndpointNaver.SENS_SEND_SMS.getUrl() + "/services/ncp:sms:kr:309403274976:waving" + "/messages";
        String signature = makeSignature(method, timestamp, accessKey, secretKey, apiUri);
        String uri = baseUri + apiUri;
        int verificationCode = generateVerificationCode();   // 인증코드 생성


        Map<String, Object> requestBodyMap = new HashMap<>();
        List<Map<String, String>> messagesList = new ArrayList<>();
        Map<String, String> messagesMap = new HashMap<>();

        requestBodyMap.put("type", "SMS");
        requestBodyMap.put("contentType", "COMM"); // COMM: 일반 메시지, AD: 광고메시지
        requestBodyMap.put("countryCode", "82");
        requestBodyMap.put("from", "01020176384");  // TODO: 전화번호 web 발신 혹은 하드코딩 제거
//            requestBodyMap.put("subject", "");    // LMS, MMS에서만 사용 가능, 최대 40byte
        String content = "인증번호: " + verificationCode;
        requestBodyMap.put("content", content);
        String cellphoneFormatted = this.formatCellphone(personalAuthRequestDto.getCellphone());
        messagesMap.put("to", cellphoneFormatted);
//        messagesMap.put("content", content);  // 개별 메시지 내용 SMS: 최대 80 byte, LMS, MMS: 최대 2000byte
        messagesList.add(messagesMap);
        requestBodyMap.put("messages", messagesList);
//            requestBodyMap.put("files", "");              // MMS 에서만 사용가능
//            requestBodyMap.put("reserveTime", "");        // 메시지 발송 예약 일시 (yyyy-MM-dd HH:mm)
//            requestBodyMap.put("reserveTimeZone", "");    // 예약 일시 타임존 (기본: Asia/Seoul)
//            requestBodyMap.put("scheduleCode", "");       // 등록하려는 스케줄 코드
        String requestBodyString = new Gson().toJsonTree(requestBodyMap).getAsJsonObject().toString();
//        System.out.println(requestBodyString);

        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.setHeader("x-ncp-apigw-timestamp", timestamp);
        httpPost.setHeader("x-ncp-iam-access-key", accessKey);
        httpPost.setHeader("x-ncp-apigw-signature-v2", signature);
        httpPost.setEntity(new StringEntity(requestBodyString));

        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(httpPost, response -> {
                if (response.getCode() == 202) {
                    redisTemplate.opsForValue().set(
                            cellphoneFormatted
                            , Integer.toString(verificationCode)
                            , 5
                            , TimeUnit.MINUTES
                    );
                    result.setMessage("success");
                    responseDto.setResult(result);
                }

                System.out.println(response.getCode() + " " + response.getReasonPhrase());
                final HttpEntity entity = response.getEntity();
                String responseBodyString = EntityUtils.toString(entity);
                System.out.println(responseBodyString);
                EntityUtils.consume(entity);

                return null;
            });
        } catch (Exception ex) {
            System.out.println("error occurred: " + ex);

            responseDto.setCode(400);
            result.setMessage("error occurred");
            responseDto.setResult(result);
        }

        return responseDto;
    }

    public ResponseDto confirmAuthenticationCode(PersonalAuthenticationVerificationDto personalAuthVerificationDto) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();


        try {
            final String cellphoneFormatted = this.formatCellphone(personalAuthVerificationDto.getCellphone());
            final String stringCode = redisTemplate.opsForValue().get(cellphoneFormatted);

            if (stringCode == null) {
                responseDto.setCode(404);
                result.setMessage("verification code expired");
            }
            else {
                final int code = Integer.parseInt(stringCode);
                if (code == personalAuthVerificationDto.getCode()) {
                    redisTemplate.opsForValue().getAndDelete(cellphoneFormatted);
                    responseDto.setCode(200);
                    result.setMessage("success");
                } else {
                    responseDto.setCode(404);
                    result.setMessage("invalid verification code");
                }
            }
        } catch (Exception ex) {
            responseDto.setCode(400);
            result.setMessage(ex.toString());
        }
        responseDto.setResult(result);

        return responseDto;
    }

    /**
     * 회원 가입 후 userId, accessToken, refreshToken 을 반환
     * @param userDto 사용자 정보
     * @return 요청 응답
     */
    @Transactional
    public ResponseDto createUser(UserJoinRequestDto userDto) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();
        try {
            // 회원 가입 전 아이디 조회, 존재 시 에러 메시지 반환
            UserEntity userEntity = this.userRepository.findByUsername(userDto.getUsername()).orElse(null);
            if (userEntity != null) {
                responseDto.setCode(400);
                result.setMessage("동일한 아이디가 존재 합니다");
                responseDto.setResult(result);

                return responseDto;
            }

            String password = userDto.getPassword();
            // 회원 테이블 저장 후 회원 아이디 조회
            this.userRepository.save(userDto.toUserEntity());
            userEntity = this.userRepository.findByUsername(userDto.getUsername()).orElseThrow();
            Long userId = userEntity.getUserId();

            // 조회 한 회원 아이디 기준으로 본인인증, 로그인 정보 저장
            PersonalAuthenticationEntity personalAuthenticationEntity = userDto.toSelfAuthenticationEntity(userId);
            this.personalAuthenticationRepository.save(personalAuthenticationEntity);
            LoginEntity loginEntity = userDto.toLoginEntity(
                    userId
                    , passwordEncoder.encode(password)
                    , encryptPasswordWithSHA256(password)
            );
            this.loginRepository.save(loginEntity);

            String accessToken = jwtService.generateToken(userEntity);
            String refreshToken = jwtService.generateRefreshToken(userEntity);
            redisTemplate.opsForValue().set(userEntity.getUsername(), refreshToken, Duration.ofMillis(jwtService.getRefreshExpiration()));

            UserJoinResponseDto userJoinResponseDto = new UserJoinResponseDto();
            userJoinResponseDto.setMessage("success");
            userJoinResponseDto.setId(userId);
            userJoinResponseDto.setAccessToken(accessToken);
            userJoinResponseDto.setRefreshToken(refreshToken);

            result.setUserJoinResponseDto(userJoinResponseDto);
            responseDto.setResult(result);
        } catch (Exception ex) {
            responseDto.setCode(400);
            result.setMessage(ex.toString());
            responseDto.setResult(result);
        }

        return responseDto;
    }

    public ResponseDto getUser(Long userId) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        try {
            UserEntity userEntity = getUserInfo(userId);
            PersonalAuthenticationEntity personalAuthenticationEntity = getPersonalAuthInfo(userId);
            UserInfoResponseDto userInfoResponseDto = UserInfoResponseDto.builder()
                    .userEntity(userEntity)
                    .personalAuthenticationEntity(personalAuthenticationEntity)
                    .build();
            result.setUserInfoResponseDto(userInfoResponseDto);
            responseDto.setResult(result);
        } catch (Exception ex) {
            responseDto.setCode(400);
            result.setMessage(ex.toString());
            responseDto.setResult(result);
        }

        return responseDto;
    }

    public ResponseDto getFavoriteGreetings(Long userId) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        return responseDto;
    }

    public ResponseDto setFavoriteGreeting(Long userId, UserFavoriteGreetingRequestDto request) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        try {
            this.saveFavoriteGreeting(userId, request);
            result.setMessage("success");
            responseDto.setResult(result);
        } catch (Exception ex) {
            responseDto.setCode(400);
            result.setMessage(ex.toString());
            responseDto.setResult(result);
        }

        return responseDto;
    }

    /**
     * 1. JWT 토큰 삭제 2. 사용자 아이디 관련된 데이터 삭제
     * @param userId 사용자 공유 번호
     * @return responseDto
     */

    @Transactional
    public ResponseDto deleteUser(Long userId) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        try {
            userRepository.findByUserId(userId).ifPresentOrElse(
                    user -> redisTemplate.delete(user.getUsername())
                    , () -> {
                        throw new EntityNotFoundException("user not found");
                    }
            );
            contactRepository.findByUserId(userId).ifPresent(
//                    contact -> friendProfileRepository.deleteByContactId(contact.getContactId())
                      // 데이터 삭제 시 delete() 함수로 soft delete (삭제 날짜 마크) 처리 하는 것이 의도 인지 모르지만 일단 활용
                      // 특이 사항: delete 날짜 업데이트 시 update 날짜도 delete날짜로 업데이트 되는거 같음
                      contact -> {
                          friendProfileRepository.findAllByContactId(contact.getContactId()).forEach(FriendProfileEntity::delete);
                      }
            );
            contactRepository.deleteByUserId(userId);
            favoriteGreetingRepository.deleteByUserId(userId);
            loginRepository.deleteByUserId(userId);
            personalAuthenticationRepository.deleteByUserId(userId);
            userRepository.deleteById(userId);

            result.setMessage("success");
        } catch (EntityNotFoundException ex) {
            responseDto.setCode(400);
            result.setMessage(ex.getMessage());
        }
        catch (Exception ex) {
            responseDto.setCode(400);
            System.out.println(ex.getMessage());
            result.setMessage("Oops, something went wrong..");
        }
        finally {
            responseDto.setResult(result);
        }

        return responseDto;
    }
}
