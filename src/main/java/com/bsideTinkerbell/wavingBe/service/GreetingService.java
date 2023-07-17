package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.GreetingCategoryDto;
import com.bsideTinkerbell.wavingBe.domain.dto.GreetingDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseResultDto;
import com.bsideTinkerbell.wavingBe.domain.entity.GreetingCategoryEntity;
import com.bsideTinkerbell.wavingBe.domain.entity.GreetingEntity;
import com.bsideTinkerbell.wavingBe.repository.GreetingCategoryRepository;
import com.bsideTinkerbell.wavingBe.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GreetingService {
    private final GreetingCategoryRepository greetingCategoryRepository;
    private final GreetingRepository greetingRepository;

    private GreetingEntity findGreeting() {
        return greetingRepository.findFirstByOrderByGreeting().orElseThrow();
    }

    private List<GreetingEntity> findGreetingsByGreetingCategoryId(Long id) {
        return greetingRepository.findByGreetingCategoryId(id);
    }

    private List<GreetingCategoryEntity> findGreetingCategories() {
        return greetingCategoryRepository.findAll();
    }

    public ResponseDto getGreeting() {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        GreetingDto greetingDto = GreetingDto.builder().greetingEntity(findGreeting()).build();
        result.setGreetingDto(greetingDto);
        responseDto.setCode(200);
        responseDto.setResult(result);

        return responseDto;
    }

    /**
     * 인사말 저장 방법 정해지면 categoryId로 조회하는걸로 변경 예정
     * @param categoryId 카테고리 아이디
     * @return responseDto 응답 객체
     */
    public ResponseDto getGreetingsByGreetingCategoryId(Long categoryId) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        List<GreetingDto> greetingDtoList = new ArrayList<>();
        List<GreetingEntity> greetings = findGreetingsByGreetingCategoryId(categoryId);
        for (GreetingEntity greeting: greetings) {
            greetingDtoList.add(GreetingDto.builder().greetingEntity(greeting).build());
        }
        result.setGreetingDtoList(greetingDtoList);

        responseDto.setCode(200);
        responseDto.setResult(result);

        return responseDto;
    }

    public ResponseDto getGreetingCategory() {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();
        List<GreetingCategoryDto> greetingCategoryDtoList = new ArrayList<>();
        List<GreetingCategoryEntity> greetingCategories = findGreetingCategories();

        for (GreetingCategoryEntity greetingCategory: greetingCategories) {
            greetingCategoryDtoList.add(GreetingCategoryDto.builder().greetingCategoryEntity(greetingCategory).build());
        }
        result.setGreetingCategoryDtoList(greetingCategoryDtoList);

        responseDto.setCode(200);
        responseDto.setResult(result);

        return responseDto;
    }
}
