package com.bsideTinkerbell.wavingBe.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PersonalAuthenticationRequestDto {
    @NotBlank
    @Size(min = 13, max = 13, message = "length must be 13")
//    @Pattern(regexp = "\\d{3}-?\\d{4}-?\\d{4}")
    @Pattern(regexp = "\\d{3}-\\d{4}-\\d{4}", message = "must be in xxx-xxxx-xxxx format")
    private String cellphone;   // 비밀번호 반드시 존재해야 하고 xxxxxxxxxxx/xxx-xxxx-xxxx 형식의 11자리 숫자 문자열
}
