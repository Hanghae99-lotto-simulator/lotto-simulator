package com.lotto.lotto_simulator.controller.responseDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LankRoundDto {
    private  Long id;
    private Long Count;
    private LocalDateTime data;
    private List<Byte> RoundArray;
    private Byte BonusNum;
    private Integer firstRank;
    private Integer secondRank;
    private Integer thirdRank;
    private Integer fourthRank;
    private Integer fifthRank;
}
