package com.lotto.lotto_simulator.controller.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LottoResponseDto {

    private Byte firstNum;
    private Byte secondNum;
    private Byte thirdNum;
    private Byte fourthNum;
    private Byte fifthNum;
    private Byte sixthNum;
    private String uniqueCode;
}
