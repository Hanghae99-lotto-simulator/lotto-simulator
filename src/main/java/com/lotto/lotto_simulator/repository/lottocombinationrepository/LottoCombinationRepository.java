package com.lotto.lotto_simulator.repository.lottocombinationrepository;

import com.lotto.lotto_simulator.entity.LottoCombination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LottoCombinationRepository extends JpaRepository<LottoCombination,Long> ,LottoCombinationCustom{
    @Query(value="SELECT * FROM lotto_combination l JOIN(" +
            "SELECT lotto_id FROM lotto_combination ORDER BY rand() limit :num) " +
            "AS r ON l.lotto_id=r.lotto_id",nativeQuery = true)
    List<LottoCombination> randomNumss(@Param("num") Long num) ;
}
