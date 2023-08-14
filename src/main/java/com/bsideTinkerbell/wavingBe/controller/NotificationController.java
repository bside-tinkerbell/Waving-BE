package com.bsideTinkerbell.wavingBe.controller;

import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("v1/notifications")
public class NotificationController {
    public ResponseEntity<ResponseDto> getAllNotifications() {
        ResponseDto responseDto = new ResponseDto();
        return ResponseEntity.status(responseDto.getCode()).body(responseDto);
    }
}
