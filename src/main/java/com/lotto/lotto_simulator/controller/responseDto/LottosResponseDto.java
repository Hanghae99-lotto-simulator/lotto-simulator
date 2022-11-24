package com.lotto.lotto_simulator.controller.responseDto;


import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.requestDto.UniqueCodeDto;
import com.lotto.lotto_simulator.entity.Lotto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LottosResponseDto {
    String uniqueCode;
    List<List<Byte>> lottoArray;
    //List<LottoDto>
}
