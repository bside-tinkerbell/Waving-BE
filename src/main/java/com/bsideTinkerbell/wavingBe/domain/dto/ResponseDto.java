package com.bsideTinkerbell.wavingBe.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ResponseDto {
    private int code;
    private Map<String, Object> result;
}
