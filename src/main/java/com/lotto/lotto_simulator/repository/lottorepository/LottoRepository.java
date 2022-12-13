package com.lotto.lotto_simulator.repository.lottorepository;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.entity.Lotto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LottoRepository extends JpaRepository<Lotto,Long> ,LottoRepositoryCustom{

    List<Lotto> findAllByUniqueCode(String id);
    @Query(value = "SELECT * FROM lotto.lotto l WHERE MATCH(unique_code) AGAINST(:unique_code IN BOOLEAN MODE)",nativeQuery = true)
    List<Lotto> fullTextSearchV2(@Param("unique_code") String uniqueCode);
}
