package com.lotto.lotto_simulator.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final CustomError customError;

    public CustomException( CustomError customError){
//        this.customError.getMessage());
        this.customError = customError;
    }
}

