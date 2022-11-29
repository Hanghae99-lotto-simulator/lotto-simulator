package com.lotto.lotto_simulator.controller.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RoundWinnersResponseDto {
    private Long firstRank;
    private Long secondRank;
    private Long thirdRank;
    private Long fourthRank;
    private Long fifthRank;
    private Long lottoCnt;
}
