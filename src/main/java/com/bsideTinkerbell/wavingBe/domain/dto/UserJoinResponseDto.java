package com.bsideTinkerbell.wavingBe.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class UserJoinResponseDto extends AuthenticationResponseDto {
    private String message;
}
