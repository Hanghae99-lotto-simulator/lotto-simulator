package com.lotto.lotto_simulator.repository;


import com.lotto.lotto_simulator.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {
    Optional<Store> findById(Long id);
}
