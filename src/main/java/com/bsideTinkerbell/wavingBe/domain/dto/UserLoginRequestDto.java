package com.bsideTinkerbell.wavingBe.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequestDto {
    private String username;
    private String password;
}
