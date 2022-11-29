package com.lotto.lotto_simulator.repository.lottocombinationrepository;

import com.lotto.lotto_simulator.controller.requestDto.LottoCombinationDto;
import com.lotto.lotto_simulator.entity.LottoCombination;

import java.util.List;

public interface LottoCombinationCustom {
    List<LottoCombination> searchAll();
    List<LottoCombinationDto> randomNums(Long id);
}
