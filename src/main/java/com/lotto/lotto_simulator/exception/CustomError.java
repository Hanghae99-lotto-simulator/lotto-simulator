package com.lotto.lotto_simulator.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum CustomError {
    //400 BAD_REQUEST 잘못된 요청
    INVALID_PARAMETER(HttpStatus.NOT_ACCEPTABLE.value(), "M001", "1 이상의 수만 입력할 수 있습니다."),
    UNIQUE_CODE_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "M002", "일치하는 유니크 코드가 없습니다."),
    UNIQUE_CODE_NULL(HttpStatus.BAD_REQUEST.value(),"M003","유니크 코드가 공백입니다 유니크 코드를 입력해주세요."),
    ROUND_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "M004", "일치하는 회차가 없습니다.");



    private final int status;
    private final String code;
    private final String message;
}
