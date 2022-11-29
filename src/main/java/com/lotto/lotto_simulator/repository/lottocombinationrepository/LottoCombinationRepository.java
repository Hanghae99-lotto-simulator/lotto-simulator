package com.lotto.lotto_simulator.repository.lottocombinationrepository;

import com.lotto.lotto_simulator.entity.LottoCombination;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface LottoCombinationRepository extends JpaRepository<LottoCombination,Long> ,LottoCombinationCustom{

}
