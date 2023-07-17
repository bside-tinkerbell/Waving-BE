package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.GreetingCategoryEntity;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GreetingCategoryDto {
    @JsonProperty("greeting_category_id")
    private Long greetingCategoryId;
    private String category;

    @Builder
    public GreetingCategoryDto(GreetingCategoryEntity greetingCategoryEntity) {
        this.greetingCategoryId = greetingCategoryEntity.getGreetingCategoryId();
        this.category = greetingCategoryEntity.getCategory();
    }
}
