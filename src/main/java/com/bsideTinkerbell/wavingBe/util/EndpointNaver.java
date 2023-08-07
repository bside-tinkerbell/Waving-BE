package com.bsideTinkerbell.wavingBe.util;

import lombok.Getter;

/**
 * Naver API 서비스 관련 엔드 포인트 유틸
 */
@Getter
public enum EndpointNaver {
    SENS_SEND_SMS("/sms/v2"),
    SENS_SEND_PUSH("/push/v2"),
    SENS_SEND_ALIMTALK("/alimtalk/v2"),
    SENS_SEND_FRIENDTALK("/friendtalk/v2")
    ;

    private final String url;

    EndpointNaver(String url) {
        this.url = url;
    }
}
