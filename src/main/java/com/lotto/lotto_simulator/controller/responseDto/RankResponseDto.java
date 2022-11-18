package com.lotto.lotto_simulator.controller.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RankResponseDto {
    private Integer firstRank;
    private Integer secondRank;
    private Integer thirdRank;
    private Integer fourthRank;
    private Integer fifthRank;
}
