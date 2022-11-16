package com.lotto.lotto_simulator.controller.requestDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
public class LottoDto {

    private Long firstNum;
    private Long secondNum;
    private Long thirdNum;
    private Long fourthNum;
    private Long fifthNum;
    private Long sixthNum;

    private String store;
    private boolean auto;
    @QueryProjection
    public LottoDto(Long firstNum, Long secondNum, Long thirdNum, Long fourthNum, Long fifthNum, Long sixthNum, boolean auto) {
        this.firstNum = firstNum;
        this.secondNum = secondNum;
        this.thirdNum = thirdNum;
        this.fourthNum = fourthNum;
        this.fifthNum = fifthNum;
        this.sixthNum = sixthNum;
        this.auto = auto;
    }
}
