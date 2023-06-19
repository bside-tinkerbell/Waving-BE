package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.PersonalAuthenticationRequestDto;
import com.bsideTinkerbell.wavingBe.domain.dto.PersonalAuthenticationVerificationDto;
import com.bsideTinkerbell.wavingBe.domain.dto.UserDto;
import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.PersonalAuthenticationEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import com.bsideTinkerbell.wavingBe.repository.LoginRepository;
import com.bsideTinkerbell.wavingBe.repository.PersonalAuthenticationRepository;
import com.bsideTinkerbell.wavingBe.repository.UserRepository;
import com.bsideTinkerbell.wavingBe.util.EndpointNaver;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
//import org.apache.hc.core5.net.URIBuilder;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.utils.Base64;
import org.apache.hc.core5.http.ClassicHttpResponse;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.HttpException;
import org.apache.hc.core5.http.io.HttpClientResponseHandler;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
//import java.net.URI;
//import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
    private final PersonalAuthenticationRepository personalAuthenticationRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public UserService(
            UserRepository userRepository
            , LoginRepository loginRepository
            , PersonalAuthenticationRepository personalAuthenticationRepository
            , RedisTemplate<String, String> redisTemplate
    ) {
        this.userRepository = userRepository;
        this.loginRepository = loginRepository;
        this.personalAuthenticationRepository = personalAuthenticationRepository;
        this.redisTemplate = redisTemplate;
    }

    @Transactional
    public void createUser(UserDto userDto) throws Exception, NoSuchAlgorithmException, InvalidKeySpecException {
        UserEntity userEntity = userDto.toUserEntity();
        this.userRepository.save(userEntity);
        UserEntity userOptionalEntity = this.userRepository.findByUsername(userDto.getUsername()).orElse(null);
        if (userOptionalEntity == null) {
            throw new Exception("아이디가 존재하지 않습니다");
        }
        PersonalAuthenticationEntity selfAuthenticationEntity = userDto.toSelfAuthenticationEntity(userOptionalEntity.getUserId());
        LoginEntity loginEntity = userDto.toLoginEntity(userOptionalEntity.getUserId());
        this.personalAuthenticationRepository.save(selfAuthenticationEntity);
        this.loginRepository.save(loginEntity);
    }

    public String makeSignature(String method, String timestamp, String accessKey, String secretKey, String url) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";					// one space
        String newLine = "\n";				// new line
        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();
        System.out.println(message);
        SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));

        return Base64.encodeBase64String(rawHmac);
    }

    /**
     * nCloud SENS 통해 휴대폰 인증번호 전송
     * @param personalAuthRequestDto 본인인증 데이터 전송 객체
     */
    public void sendAuthenticationCode(PersonalAuthenticationRequestDto personalAuthRequestDto) throws NoSuchAlgorithmException, InvalidKeyException {
        String method = "POST";                                         // method
        String timestamp = String.valueOf(System.currentTimeMillis());	// current timestamp (epoch)
        String accessKey = "Ims2TbuxYF9fPCg9G2n6";			            // access key id (from portal or Sub Account)
        String secretKey = "8VwKPaIodjIu8YP1cTsW8KIKqkVJfaSBOBU7UwJF";

        String baseUri = "https://sens.apigw.ntruss.com";
        String apiUri = EndpointNaver.SENS_SEND_SMS.getUrl() + "/services/ncp:sms:kr:309403274976:waving" + "/messages";
        String xNcpApigwSignatureV2 = makeSignature(method, timestamp, accessKey, secretKey, apiUri);
        System.out.println(method + " " + timestamp + " " + accessKey + " " + xNcpApigwSignatureV2);

        String uri = baseUri + apiUri;
        System.out.println(uri);
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader("Content-Type", "application/json; charset=utf-8");
        httpPost.setHeader("x-ncp-apigw-timestamp", timestamp);
        httpPost.setHeader("x-ncp-iam-access-key", accessKey);
        httpPost.setHeader("x-ncp-apigw-signature-v2", xNcpApigwSignatureV2);
        Gson gson = new Gson();
        Random rand = new Random();
        int authenticationCode = rand.nextInt(90000) + 10000;   // 인증코드 생성
        final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        stringStringValueOperations.set(personalAuthRequestDto.getCellphone(), Integer.toString(authenticationCode), 5, TimeUnit.MINUTES);

        Map<String, Object> requestBodyMap = new HashMap<>();
        requestBodyMap.put("type", "SMS");
        requestBodyMap.put("contentType", "COMM"); // COMM: 일반 메시지, AD: 광고메시지
        requestBodyMap.put("countryCode", "82");
        requestBodyMap.put("from", "01020176384");
//            requestBodyMap.put("subject", "");
        String content = "인증번호: " + String.valueOf(authenticationCode);
        requestBodyMap.put("content", content);
        List<Map<String, String>> messagesList = new ArrayList<>();
        Map<String, String> messagesMap = new HashMap<>();
        messagesMap.put("to", personalAuthRequestDto.getCellphone());
        messagesMap.put("content", content);
        System.out.println(gson.toJsonTree(messagesMap).getAsJsonObject().toString());
        messagesList.add(messagesMap);
        requestBodyMap.put("messages", messagesList);
//            requestBodyMap.put("files", "");
//            requestBodyMap.put("reserveTime", "");
//            requestBodyMap.put("reserveTimeZone", "");
//            requestBodyMap.put("scheduleCode", "");
        String requestBodyString = gson.toJsonTree(requestBodyMap).getAsJsonObject().toString();
        System.out.println(requestBodyString);
        httpPost.setEntity(new StringEntity(requestBodyString));
        HttpClientResponseHandler<CloseableHttpResponse> handler = new HttpClientResponseHandler<CloseableHttpResponse>() {
            @Override
            public CloseableHttpResponse handleResponse(ClassicHttpResponse response) throws HttpException, IOException {
                return (CloseableHttpResponse) response;
            }
        };

        try(CloseableHttpClient client = HttpClients.createDefault()) {
            CloseableHttpResponse response = client.execute(httpPost, handler);
            HttpEntity entity = response.getEntity();
            System.out.println("response: " + EntityUtils.toString(entity));
        } catch (Exception ex) {
            System.out.println("error occurred: " + ex.toString());
        }
    }

    public String verifyAuthenticationCode(PersonalAuthenticationVerificationDto personalAuthVerificationDto) {
        final String cellphone = personalAuthVerificationDto.getCellphone();
        final ValueOperations<String, String> stringStringValueOperations = redisTemplate.opsForValue();
        final String stringCode = stringStringValueOperations.get(cellphone);

        if (stringCode == null)
            return "Verification Code Expired";
        else {
            final int code = Integer.parseInt(stringCode);
            if (code == personalAuthVerificationDto.getCode()) {
                redisTemplate.opsForValue().getAndDelete(cellphone);
                return "Verification Success";
            }
            else
                return "Invalid Verification Code";
        }
    }
}
