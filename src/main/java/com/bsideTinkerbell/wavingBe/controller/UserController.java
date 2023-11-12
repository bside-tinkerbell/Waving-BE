package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.*;
import com.bsideTinkerbell.wavingBe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
     * @param request 회원가입 요청 정보
     * @return 회원가입 성공여부
     */
    @PostMapping("/join")
    public ResponseEntity<ResponseDto> createUser(@RequestBody @Valid UserJoinRequestDto request) {
        ResponseDto responseDto = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    /**
     * 회원 정보 질의
     * @param userId 회원 아이디 (seq)를 가지고 요청
     * @return 회원 정보
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ResponseDto> getUser(@PathVariable Long userId) {
        ResponseDto responseDto = userService.getUser(userId);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    @GetMapping("/{userId}/favorite-greetings")
    public ResponseEntity<ResponseDto> getFavoriteGreetings(@PathVariable Long userId) {
        ResponseDto responseDto = userService.getFavoriteGreetings(userId);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    /**
     * 회원의 인사말 즐겨 찾기 생성
     * @param userId 회원 아이디 (pk seq)
     * @param id
     * @return 즐겨 찾기 생성 성공 여부
     */
    @PostMapping("/{userId}/favorite-greetings")
    public ResponseEntity<ResponseDto> createFavoriteGreetings(
            @PathVariable Long userId
            , @RequestBody UserFavoriteGreetingRequestDto request) {
        ResponseDto responseDto = userService.setFavoriteGreeting(userId, request);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<ResponseDto> deleteUser(@PathVariable Long userId) {
        ResponseDto responseDto = userService.deleteUser(userId);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }
}
