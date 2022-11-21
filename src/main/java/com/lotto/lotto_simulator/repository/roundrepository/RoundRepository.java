package com.lotto.lotto_simulator.repository.roundrepository;

import com.lotto.lotto_simulator.entity.Round;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoundRepository extends JpaRepository<Round, Long> ,RoundRepositoryCustom{


}
