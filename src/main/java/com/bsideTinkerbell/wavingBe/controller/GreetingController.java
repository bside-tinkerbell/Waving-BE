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

    @GetMapping("/main/{categoryName}")
    public ResponseEntity<ResponseDto> getGreetingsByCategory(@PathVariable("categoryName") String categoryName) {
        ResponseDto responseDto = greetingService.getGreetingsByCategoryName(categoryName);
        return ResponseEntity.status(HttpStatus.valueOf(responseDto.getCode())).body(responseDto);
    }
}
