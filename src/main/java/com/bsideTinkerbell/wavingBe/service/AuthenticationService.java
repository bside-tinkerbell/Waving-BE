package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.AuthenticationResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseResultDto;
import com.bsideTinkerbell.wavingBe.domain.dto.UserLoginRequestDto;
import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import com.bsideTinkerbell.wavingBe.repository.LoginRepository;
import com.bsideTinkerbell.wavingBe.repository.UserRepository;
import com.bsideTinkerbell.wavingBe.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
//    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     *
     * @param username 사용자 아이디 (이메일)
     * @param password 암호화 되지 않은 비밀번호
     * @return 사용자
     */
    private UserEntity authenticate(String username, String password) {
        UserEntity user = userRepository.findByUsername(username).orElse(null);
        if (user == null)
            return null;
        LoginEntity login = loginRepository.findById(user.getUserId()).orElse(null);
        if (login != null && passwordEncoder.matches(password, login.getSalt())) {
            return user;
        }
        return null;
    }

//    public UserEntity authenticate(UserLoginRequestDto userLoginRequestDto) {
//        System.out.println("authenticating\n" + userLoginRequestDto);
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                userLoginRequestDto.getUsername(),
//                passwordEncoder.encode(userLoginRequestDto.getPassword())
//            )
//        );
//        return userRepository.findByUsername(userLoginRequestDto.getUsername()).orElseThrow();
//    }

    public ResponseDto loginUser(UserLoginRequestDto userLoginRequestDto) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();

        // 아이디(이메일), 비번 둘중 하나라도 없으면 에러 반환
        if (username == null || password == null) {
            responseDto.setCode(400);
            result.setMessage("username and password are required");
            responseDto.setResult(result);
            return responseDto;
        }

        // 사용자 인증
        UserEntity user =  this.authenticate(username, password);
        if (user != null) {
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
            authenticationResponseDto.setId(user.getUserId());
            authenticationResponseDto.setAccessToken(accessToken);
            authenticationResponseDto.setRefreshToken(refreshToken);
            // Redis 에 Refresh Token 저장
            redisTemplate.opsForValue().set(username, refreshToken, Duration.ofMillis(jwtService.getRefreshExpiration()));
            result.setAuthenticationResponseDto(authenticationResponseDto);
        }
        else {
            responseDto.setCode(401);
            result.setMessage("invalid username or password");
        }
        responseDto.setResult(result);

        return responseDto;
    }

    /**
     *
     * @param authHeader authentication header
     * @return True/False
     */
    public boolean validateAuthHeader(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    /**
     * Access Token 갱신을 위한 Refresh Token 갱신
     * @param authHeader Authentication Header
     * @return http response
     */
    public ResponseDto refreshToken(String authHeader) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        // refresh token 을 authorization header 에서 bearer 로 전달 받는다
        final String refreshToken;
        final String userEmail;

        if (validateAuthHeader(authHeader)) {
            responseDto.setCode(403);
            result.setMessage("Invalid authentication header");
            responseDto.setResult(result);
            return responseDto;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        // 사용자 이메일이 존재하는데 사용자가 인증되지 않았을때
        if (userEmail != null) {
            UserDetails userDetails = this.userRepository.findByUsername(userEmail).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtService.generateRefreshToken(userDetails);
                AuthenticationResponseDto authenticationResponseDto = new AuthenticationResponseDto();
                authenticationResponseDto.setAccessToken(accessToken);
                authenticationResponseDto.setRefreshToken(refreshToken);
                result.setAuthenticationResponseDto(authenticationResponseDto);
                responseDto.setResult(result);
            }
        }
        return responseDto;
    }

    public ResponseDto logout(String authHeader) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();
        final String accessToken;
        final String userEmail;

        if (validateAuthHeader(authHeader)) {
            responseDto.setCode(403);
            result.setMessage("Invalid authentication header");
            responseDto.setResult(result);
            return responseDto;
        }

        accessToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(accessToken);

        redisTemplate.delete(userEmail);
        redisTemplate.opsForValue().set(
                accessToken
                , "logout"
                , Duration.ofMillis(jwtService.getAccessExpiration())
        );
        result.setMessage("logout success");
        responseDto.setResult(result);

        return responseDto;
    }
}
