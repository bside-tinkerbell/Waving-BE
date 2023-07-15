package com.bsideTinkerbell.wavingBe.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseResultDto {
    private String message;
    private GreetingDto greetingDto;
    private List<GreetingDto> greetingDtoList;
    private AuthenticationResponseDto authenticationResponseDto;
}
