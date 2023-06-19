package com.bsideTinkerbell.wavingBe.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonalAuthenticationRequestDto {
    @NotBlank
    private String cellphone;
}
