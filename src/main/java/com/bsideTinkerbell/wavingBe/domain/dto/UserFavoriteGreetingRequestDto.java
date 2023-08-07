package com.bsideTinkerbell.wavingBe.domain.dto;

import com.bsideTinkerbell.wavingBe.domain.entity.FavoriteGreetingEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 회원의 인사말 즐겨찾기 등록 DTO
 */
@Getter
@Setter
@NoArgsConstructor
public class UserFavoriteGreetingRequestDto {
    private Long greetingId;

    public FavoriteGreetingEntity toEntity(Long userId) {
        return FavoriteGreetingEntity.builder()
                .userId(userId)
                .greetingId(this.greetingId)
                .build();
    }
}
