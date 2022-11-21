package com.lotto.lotto_simulator.repository.lottorepository;


import com.lotto.lotto_simulator.controller.requestDto.LottoDto;

import java.util.List;

//쿼리디에스엘 정의 하는 곳
public interface LottoRepositoryCustom {
    List<LottoDto> search();
}
