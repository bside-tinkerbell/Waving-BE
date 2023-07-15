package com.bsideTinkerbell.wavingBe.service;

import com.bsideTinkerbell.wavingBe.domain.dto.GreetingDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseResultDto;
import com.bsideTinkerbell.wavingBe.domain.entity.GreetingEntity;
import com.bsideTinkerbell.wavingBe.repository.GreetingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GreetingService {
    private final GreetingRepository greetingRepository;

    private GreetingEntity findGreeting() {
        return greetingRepository.findFirstByOrderByGreeting().orElseThrow();
    }

    private List<GreetingEntity> findGreetingsByCategoryName(String name) {
        return greetingRepository.findByCategory(name);
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
     * @param categoryName 카테고리 이름
     * @return responseDto 응답 객체
     */
    public ResponseDto getGreetingsByCategoryName(String categoryName) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();

        List<GreetingDto> greetingDtoList = new ArrayList<>();
        List<GreetingEntity> greetings = findGreetingsByCategoryName(categoryName);
        for (GreetingEntity greeting: greetings) {
            greetingDtoList.add(GreetingDto.builder().greetingEntity(greeting).build());
        }
        result.setGreetingDtoList(greetingDtoList);

        responseDto.setCode(200);
        responseDto.setResult(result);

        return responseDto;
    }
}
