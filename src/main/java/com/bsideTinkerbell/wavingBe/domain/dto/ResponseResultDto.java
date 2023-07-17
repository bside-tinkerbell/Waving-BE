package com.bsideTinkerbell.wavingBe.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ResponseResultDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("greeting_category_list")
    private List<GreetingCategoryDto> greetingCategoryDtoList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("greeting")
    private GreetingDto greetingDto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("greeting_list")
    private List<GreetingDto> greetingDtoList;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("token")
    private AuthenticationResponseDto authenticationResponseDto;
}
