package com.bsideTinkerbell.wavingBe.util;

import com.bsideTinkerbell.wavingBe.domain.dto.ResponseDto;
import com.bsideTinkerbell.wavingBe.domain.dto.ResponseResultDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 예외 처리 핸들링
 */

@RestControllerAdvice
public class BadResponseHandler {
    /**
     *
     * @param ex 컨트롤러 유효 하지 않은 인수
     * @return 예외 처리 메시지 답은 response dto
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseDto> invalidArgumentException(MethodArgumentNotValidException ex) {
        ResponseDto responseDto = new ResponseDto();
        ResponseResultDto result = new ResponseResultDto();
        int badRequestCode = HttpStatus.BAD_REQUEST.value();
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> fieldErrors = ex.getFieldErrors();
        for (int i = 0; i < fieldErrors.size(); i++) {
            errorMessage.append(fieldErrors.get(i).getField()).append(": ").append(fieldErrors.get(i).getDefaultMessage());
            if (i < fieldErrors.size() - 1)
                errorMessage.append(", ");
        }

        responseDto.setCode(badRequestCode);

        result.setMessage("invalid request body: " + errorMessage);
        responseDto.setResult(result);

        return ResponseEntity.status(HttpStatus.valueOf(badRequestCode)).body(responseDto);
    }
}
