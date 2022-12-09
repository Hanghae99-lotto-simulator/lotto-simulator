package com.lotto.lotto_simulator.repository.lottorepository;

import com.lotto.lotto_simulator.entity.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LottoRepository extends JpaRepository<Lotto,Long> ,LottoRepositoryCustom{
    List<Lotto> findAllByUniqueCode(String id);
}
