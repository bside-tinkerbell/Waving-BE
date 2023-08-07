package com.bsideTinkerbell.wavingBe.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {
    private int code = 200;
    private ResponseResultDto result;
}

