package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.GreetingEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GreetingDto {
    @JsonProperty("greeting_category_id")
    private Long greetingCategoryId;
    private String greeting;

    @Builder
    public GreetingDto(GreetingEntity greetingEntity) {
        this.greetingCategoryId = greetingEntity.getGreetingCategoryId();
        this.greeting = greetingEntity.getGreeting();
    }
}
