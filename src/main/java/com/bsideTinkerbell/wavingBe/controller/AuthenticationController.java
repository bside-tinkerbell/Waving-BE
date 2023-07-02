package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.UserLoginRequestDto;
import com.bsideTinkerbell.wavingBe.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/refresh-token")
    public ResponseEntity<ResponseDto> refreshToken(HttpServletRequest request) {
        ResponseDto responseDto = authenticationService.refreshToken(request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }
}
