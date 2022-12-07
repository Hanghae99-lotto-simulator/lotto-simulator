package com.lotto.lotto_simulator.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomResponseBody {
    private int httpStatus;
    private String code;
    private String message;

    public CustomResponseBody(CustomError customError) {
        this.httpStatus = customError.getStatus();
        this.code = customError.getCode();
        this.message = customError.getMessage();
    }
}

