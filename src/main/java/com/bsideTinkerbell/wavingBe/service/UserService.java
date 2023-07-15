package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.*;
import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.PersonalAuthenticationEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import com.bsideTinkerbell.wavingBe.repository.LoginRepository;
import com.bsideTinkerbell.wavingBe.repository.PersonalAuthenticationRepository;
import com.bsideTinkerbell.wavingBe.repository.UserRepository;
import com.bsideTinkerbell.wavingBe.util.EndpointNaver;
import com.google.gson.Gson;
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
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final PersonalAuthenticationRepository personalAuthenticationRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    private String makeSignature(String method, String timestamp, String accessKey, String secretKey, String url) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";					// one space
        String newLine = "\n";				// new line
        String message = method +
                space +
                url +
                newLine +
                timestamp +
                newLine +
                accessKey;
//        System.out.println(message);
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
    private String encryptPasswordWithSHA512(String password) throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt);
        byte[] hashedPassword = md.digest(password.getBytes(StandardCharsets.UTF_8));

        return new String(hashedPassword);
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
        messagesMap.put("to", personalAuthRequestDto.getCellphone());
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
//        Map<String, Object> result = new HashMap<>();

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            client.execute(httpPost, response -> {
                if (response.getCode() == 202) {
                    redisTemplate.opsForValue().set(
                            personalAuthRequestDto.getCellphone()
                            , Integer.toString(verificationCode)
                            , 5
                            , TimeUnit.MINUTES
                    );
                    responseDto.setCode(200);
                    result.setMessage("success");
//                    responseDto.setResult(result);
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
            String cellphone = personalAuthVerificationDto.getCellphone();
            String stringCode = redisTemplate.opsForValue().get(cellphone);

            if (stringCode == null) {
                responseDto.setCode(404);
                result.setMessage("verification code expired");
            }
            else {
                final int code = Integer.parseInt(stringCode);
                if (code == personalAuthVerificationDto.getCode()) {
                    redisTemplate.opsForValue().getAndDelete(cellphone);
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
     * TODO: 회원가입 후 accessToken을 반환할지 말지에 대한 여부
     * @param userDto 사용자 정보
     * @return 요청 응답
     */
    @Transactional
    public ResponseDto createUser(UserDto userDto) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();
        try {
            UserEntity userEntity = userDto.toUserEntity();
            String password = userDto.getPassword();
            this.userRepository.save(userEntity);

            // 회원 테이블 저장 후 회원 아이디 조회
            UserEntity userOptionalEntity = this.userRepository.findByUsername(userDto.getUsername()).orElse(null);
            if (userOptionalEntity == null) {
                throw new Exception("아이디가 존재하지 않습니다");
            }

            // 조회 한 회원 아이디 기준으로 본인인증, 로그인 정보 저장
            PersonalAuthenticationEntity personalAuthenticationEntity = userDto.toSelfAuthenticationEntity(
                    userOptionalEntity.getUserId());
            LoginEntity loginEntity = userDto.toLoginEntity(
                    userOptionalEntity.getUserId()
                    , passwordEncoder.encode(password)
                    , encryptPasswordWithSHA512(password)
            );
            this.personalAuthenticationRepository.save(personalAuthenticationEntity);
            this.loginRepository.save(loginEntity);

            responseDto.setCode(200);
            result.setMessage("success");
            responseDto.setResult(result);
        } catch (Exception ex) {
            responseDto.setCode(400);
            result.setMessage(ex.toString());
            responseDto.setResult(result);
        }

        return responseDto;
    }
}
