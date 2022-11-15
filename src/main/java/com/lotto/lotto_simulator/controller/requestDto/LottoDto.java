package com.lotto.lotto_simulator.controller.requestDto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LottoDto {

    private Long firstNum;
    private Long secondNum;
    private Long thirdNum;
    private Long fourthNum;
    private Long fifthNum;
    private Long sixthNum;
    private boolean auto;
}
