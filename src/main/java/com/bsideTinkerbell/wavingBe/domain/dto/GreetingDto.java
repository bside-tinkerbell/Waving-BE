package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.GreetingEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GreetingDto {
    private String category;
    private String greeting;

    @Builder
    public GreetingDto(GreetingEntity greetingEntity) {
        this.category = greetingEntity.getCategory();
        this.greeting = greetingEntity.getGreeting();
    }
}
