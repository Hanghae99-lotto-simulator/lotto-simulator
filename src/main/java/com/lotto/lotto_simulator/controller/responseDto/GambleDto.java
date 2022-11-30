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
    private List<List<Byte>> firstList = new ArrayList<>();
    private List<List<Byte>> secondList = new ArrayList<>();
    private List<List<Byte>> thirdList = new ArrayList<>();
    private Long fourthRank;
    private Long fifthRank;
    private Integer totalCnt;
}
