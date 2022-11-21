package com.lotto.lotto_simulator.controller.responseDto;

import com.lotto.lotto_simulator.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LottoResponseDto {
    private Long firstNum;
    private Long secondNum;
    private Long thirdNum;
    private Long fourthNum;
    private Long fifthNum;
    private Long sixthNum;
    private String uniqueCode;
    private Store store;
}
