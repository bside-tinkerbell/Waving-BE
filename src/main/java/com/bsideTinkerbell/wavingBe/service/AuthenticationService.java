package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.UserLoginRequestDto;
import com.bsideTinkerbell.wavingBe.domain.entity.LoginEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.UserEntity;
import com.bsideTinkerbell.wavingBe.repository.LoginRepository;
import com.bsideTinkerbell.wavingBe.repository.UserRepository;
import com.bsideTinkerbell.wavingBe.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final LoginRepository loginRepository;
//    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

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
        Map<String, Object> result = new HashMap<>();

        String username = userLoginRequestDto.getUsername();
        String password = userLoginRequestDto.getPassword();

        // 아이디(이메일), 비번 둘중 하나라도 없으면 에러 반환
        if (username == null || password == null) {
            responseDto.setCode(400);
            result.put("message", "username and password are required");
            return responseDto;
        }

        // 사용자 인증
        UserEntity user =  this.authenticate(username, password);
        if (user != null) {
            String accessToken = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            result.put("accessToken", accessToken);
            result.put("refreshToken", refreshToken);
            responseDto.setCode(200);
        }
        else {
            responseDto.setCode(401);
            result.put("message", "invalid username or password");
        }
        responseDto.setResult(result);

        return responseDto;
    }

    /**
     * Access Token 갱신을 위한 Refresh Token 갱신
     * @param request http 요청
     * @return http response
     */
    public ResponseDto refreshToken(HttpServletRequest request) {
        ResponseDto responseDto = new ResponseDto();
        Map<String, Object> result = new HashMap<>();

        // refresh token을 authorization header에서 bearer로 전달 받는다
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        // 사용자 이메일이 존재하는데 사용자가 인증되지 않았을때
        if (userEmail != null) {
            UserDetails userDetails = this.userRepository.findByUsername(userEmail).orElseThrow();

            if (jwtService.isTokenValid(refreshToken, userDetails)) {
                String accessToken = jwtService.generateToken(userDetails);
                result.put("accessToken", accessToken);
                result.put("refreshToken", refreshToken);
                responseDto.setCode(200);
                responseDto.setResult(result);
            }
        }
        return responseDto;
    }
}
