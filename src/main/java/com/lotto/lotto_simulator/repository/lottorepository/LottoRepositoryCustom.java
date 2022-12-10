package com.lotto.lotto_simulator.repository.lottorepository;


import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.requestDto.LottoIdDto;

import java.util.List;
import java.util.Optional;

//쿼리디에스엘 정의 하는 곳
public interface LottoRepositoryCustom {
    List<LottoDto> search();

    List<LottoDto> uniqueCodeSearch(String uniqueCode);

    List<LottoDto> improvedSearch(Long previousCount);
    List<LottoDto> fullTextSearch(String uniqueCode);

    LottoIdDto countId();

    Long countQuery();

}
