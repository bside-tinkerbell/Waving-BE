package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/greetings")
public class GreetingController {
    private final GreetingService greetingService;

    @GetMapping("/main")
    public ResponseEntity<ResponseDto> getGreetings() {
        ResponseDto responseDto = greetingService.getGreeting();
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    @GetMapping("/main/{categoryId}")
    public ResponseEntity<ResponseDto> getGreetingsByCategory(@PathVariable("categoryId") Long categoryId) {
        ResponseDto responseDto = greetingService.getGreetingsByGreetingCategoryId(categoryId);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }

    @GetMapping("/main/greeting-categories")
    public ResponseEntity<ResponseDto> getGreetingCategory() {
        ResponseDto responseDto = greetingService.getGreetingCategory();
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }
}
