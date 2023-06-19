package com.bsideTinkerbell.wavingBe.util;

/**
 * Naver API 서비스 관련 엔드포인트 유틸
 */
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

    public String getUrl() {
        return this.url;
    }
}
