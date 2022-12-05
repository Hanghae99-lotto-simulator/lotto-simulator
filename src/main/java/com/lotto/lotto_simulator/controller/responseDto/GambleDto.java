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
    //todo : 빌더 옵션으로 아래 워닝 제거하기
    /*
/Users/feyman/lotto-simulator/src/main/java/com/lotto/lotto_simulator/controller/responseDto/GambleDto.java:16: warning: @Builder will ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
    private List<List<Byte>> secondList = new ArrayList<>();
                             ^
/Users/feyman/lotto-simulator/src/main/java/com/lotto/lotto_simulator/controller/responseDto/GambleDto.java:17: warning: @Builder will ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
    private List<List<Byte>> thirdList = new ArrayList<>();
                             ^
/Users/feyman/lotto-simulator/src/main/java/com/lotto/lotto_simulator/entity/Store.java:34: warning: @Builder will ignore the initializing expression entirely. If you want the initializing expression to serve as default, add @Builder.Default. If it is not supposed to be settable during building, make the field final.
    private List<Lotto> lottos = new ArrayList<>();

     */
    private List<List<Byte>> firstList = new ArrayList<>();
    private List<List<Byte>> secondList = new ArrayList<>();
    private List<List<Byte>> thirdList = new ArrayList<>();
    private Long fourthRank;
    private Long fifthRank;
    private Integer totalCnt;
}
