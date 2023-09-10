package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.UserLoginRequestDto;
import com.bsideTinkerbell.wavingBe.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDto> login(@RequestBody UserLoginRequestDto request) {
        System.out.println(request);
        ResponseDto responseDto = authenticationService.loginUser(request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    // HttpServletRequest는 Service layer가 아닌 Controller layer에서 처리

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto> refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        ResponseDto responseDto = authenticationService.refreshToken(authHeader);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    @PatchMapping("/logout")
    public ResponseEntity<ResponseDto> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        ResponseDto responseDto = authenticationService.logout(authHeader);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }
}
