package com.lotto.lotto_simulator.exception;

import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({CustomException.class})
    public ResponseDto<?> customExceptionHandler(CustomException e) {
        int errHttpStatus = e.getCustomError().getStatus();
        String errCode = e.getCustomError().getCode();
        String errMessage = e.getCustomError().getMessage();
        CustomResponseBody customResponseBody = new CustomResponseBody();
        customResponseBody.setHttpStatus(errHttpStatus);
        customResponseBody.setCode(errCode);
        customResponseBody.setMessage(errMessage);
        return ResponseDto.fail(
                customResponseBody
        );
    }
}