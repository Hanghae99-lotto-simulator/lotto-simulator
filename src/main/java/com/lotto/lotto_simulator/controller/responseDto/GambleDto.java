package com.lotto.lotto_simulator.controller.responseDto;


import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class GambleDto {
    @Builder.Default
    private List<List<Byte>> firstList = new ArrayList<>();

    @Builder.Default
    private List<List<Byte>> secondList = new ArrayList<>();

    @Builder.Default
    private List<List<Byte>> thirdList = new ArrayList<>();

    private Long fourthRank;
    private Long fifthRank;
    private Integer totalCnt;
}
