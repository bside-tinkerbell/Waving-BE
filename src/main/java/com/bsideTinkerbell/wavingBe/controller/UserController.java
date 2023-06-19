package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.PersonalAuthenticationRequestDto;
import com.bsideTinkerbell.wavingBe.domain.dto.PersonalAuthenticationVerificationDto;
import com.bsideTinkerbell.wavingBe.domain.dto.UserDto;
import com.bsideTinkerbell.wavingBe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

/**
 * 회원 서비스 API REST 엔드포인트 제공을 위한 Controller
 */
@RestController
@RequestMapping("v1/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/join")
    public void createUser(@RequestBody @Valid UserDto request)
            throws Exception, NoSuchAlgorithmException, InvalidKeySpecException {
        userService.createUser(request);
//        int resp = 0;
//        return ResponseEntity.status(HttpStatus.OK).body(resp);
    }

    @PostMapping("/authentication")
    public void sendAuthenticationCode(@RequestBody @Valid PersonalAuthenticationRequestDto request) throws NoSuchAlgorithmException, InvalidKeyException {
        userService.sendAuthenticationCode(request);
    }

    @PostMapping("/authentication-confirm")
    public String confirmAuthenticationCode(@RequestBody @Valid PersonalAuthenticationVerificationDto request) {
        return userService.verifyAuthenticationCode(request);
    }
}
