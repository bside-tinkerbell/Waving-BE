package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/greetings")
public class GreetingController {
    private final GreetingService greetingService;

    @GetMapping("/main")
    public ResponseEntity<ResponseDto> getGreeting() {
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
