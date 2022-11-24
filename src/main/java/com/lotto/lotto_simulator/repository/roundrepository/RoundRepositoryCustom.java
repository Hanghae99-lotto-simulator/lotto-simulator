package com.lotto.lotto_simulator.repository.roundrepository;

import com.lotto.lotto_simulator.entity.Round;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface RoundRepositoryCustom {
   Optional<Round> findByRound(Long num);
   Page <Round> pageable(Pageable pageable);

}
