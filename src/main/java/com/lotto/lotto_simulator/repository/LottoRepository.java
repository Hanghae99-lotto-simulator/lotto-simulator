package com.lotto.lotto_simulator.repository;

import com.lotto.lotto_simulator.entity.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LottoRepository extends JpaRepository<Lotto,Long> ,LottoRepositoryCustom{

}
