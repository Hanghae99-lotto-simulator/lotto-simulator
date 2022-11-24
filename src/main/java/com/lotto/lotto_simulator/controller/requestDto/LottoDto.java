package com.lotto.lotto_simulator.controller.requestDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Data
@NoArgsConstructor
public class LottoDto {

    private Byte firstNum;
    private Byte secondNum;
    private Byte thirdNum;
    private Byte fourthNum;
    private Byte fifthNum;
    private Byte sixthNum;

    private String store;
    @QueryProjection
    public LottoDto(Byte firstNum, Byte secondNum, Byte thirdNum, Byte fourthNum, Byte fifthNum, Byte sixthNum) {
        this.firstNum = firstNum;
        this.secondNum = secondNum;
        this.thirdNum = thirdNum;
        this.fourthNum = fourthNum;
        this.fifthNum = fifthNum;
        this.sixthNum = sixthNum;
    }
}
