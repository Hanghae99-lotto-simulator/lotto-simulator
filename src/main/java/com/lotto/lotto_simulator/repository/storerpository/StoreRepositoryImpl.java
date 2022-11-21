package com.lotto.lotto_simulator.repository.storerpository;

import com.lotto.lotto_simulator.entity.QStore;
import com.lotto.lotto_simulator.entity.Store;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.lotto.lotto_simulator.entity.QStore.store;

@Repository
@RequiredArgsConstructor
public class StoreRepositoryImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public List<Store> searchAll() {
        return queryFactory
                .selectFrom(store)
                .fetch();
    }
}
