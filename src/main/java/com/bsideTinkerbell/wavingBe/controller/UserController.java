package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.*;
import com.bsideTinkerbell.wavingBe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * 회원 서비스 API 엔드포인트 제공을 위한 Controller
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("v1/users")
public class UserController {
    private final UserService userService;

    /**
     * 회원가입 중 인증번호 요청 받는 endpoint
     * @param request
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     */
    @PostMapping("/authentication")
    public ResponseEntity<ResponseDto> sendAuthenticationCode(
            @RequestBody @Valid PersonalAuthenticationRequestDto request
    ) throws NoSuchAlgorithmException, InvalidKeyException {
        ResponseDto responseDto = userService.sendAuthenticationCode(request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    @PostMapping("/authentication-confirm")
    public ResponseEntity<ResponseDto> confirmAuthenticationCode(
            @RequestBody @Valid PersonalAuthenticationVerificationDto request
    ) {
        ResponseDto responseDto = userService.confirmAuthenticationCode(request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    /**
     * 회원가입 시 회원정보 저장하는 endpoint
     * @param request
     * @return
     * @throws Exception
     */
    @PostMapping("/join")
    public ResponseEntity<ResponseDto> createUser(@RequestBody @Valid UserDto request) {
        ResponseDto responseDto = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserLoginRequestDto request) {
        ResponseDto responseDto = userService.loginUser(request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }
}
