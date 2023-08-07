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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class GreetingService {
    private final GreetingCategoryRepository greetingCategoryRepository;
    private final GreetingRepository greetingRepository;

    private long getRowCount() {
        return greetingRepository.count();
    }

    private GreetingEntity findRandomGreeting() {
        long rowCount = getRowCount();
        if (rowCount < 1L)
            return null;

        int min = 0;
        int max = (int) rowCount;

        Random random = new Random();
        int offset = random.nextInt(max - min + 1) + min;

        return greetingRepository.findWithOffset(offset);
    }

    private List<GreetingEntity> findGreetingsByGreetingCategoryId(Long id) {
        return greetingRepository.findByGreetingCategoryId(id);
    }

    private List<GreetingCategoryEntity> findGreetingCategories() {
        return greetingCategoryRepository.findAll();
    }

    @Cacheable(cacheNames = "greetingMain")
    public ResponseDto getGreeting() {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        GreetingEntity randomGreeting = findRandomGreeting();
        if (randomGreeting != null) {
            result.setGreetingCategoryId(randomGreeting.getGreetingCategoryId());
            result.setGreeting(randomGreeting.getGreeting());
        }
        else {
            responseDto.setCode(404);
            result.setMessage("OOPS! There are no greetings..");
        }

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
