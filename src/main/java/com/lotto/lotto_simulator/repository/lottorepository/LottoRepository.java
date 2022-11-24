package com.lotto.lotto_simulator.repository.lottorepository;

import com.lotto.lotto_simulator.entity.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LottoRepository extends JpaRepository<Lotto,Long> ,LottoRepositoryCustom{
    List<Lotto> findAllByUniqueCode(String id);
}
