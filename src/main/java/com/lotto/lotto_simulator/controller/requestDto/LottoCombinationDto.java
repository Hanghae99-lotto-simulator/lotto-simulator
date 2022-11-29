package com.lotto.lotto_simulator.controller.requestDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
@Builder
public class LottoCombinationDto {

    private Long Lotto_id;
    private Byte firstNum;

    private Byte secondNum;

    private Byte thirdNum;

    private Byte fourthNum;

    private Byte fifthNum;

    private Byte sixthNum;

    @QueryProjection
    public LottoCombinationDto(Long lotto_id, Byte firstNum, Byte secondNum, Byte thirdNum, Byte fourthNum, Byte fifthNum, Byte sixthNum) {
        this.Lotto_id=lotto_id;
        this.firstNum = firstNum;
        this.secondNum = secondNum;
        this.thirdNum = thirdNum;
        this.fourthNum = fourthNum;
        this.fifthNum = fifthNum;
        this.sixthNum = sixthNum;
    }
}
