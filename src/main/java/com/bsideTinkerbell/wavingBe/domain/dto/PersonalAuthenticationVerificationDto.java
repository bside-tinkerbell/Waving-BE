package com.bsideTinkerbell.wavingBe.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class PersonalAuthenticationVerificationDto extends PersonalAuthenticationRequestDto {
    @Min(10000)
    @Max(99999)
    private int code;   // 10000~99999
}
