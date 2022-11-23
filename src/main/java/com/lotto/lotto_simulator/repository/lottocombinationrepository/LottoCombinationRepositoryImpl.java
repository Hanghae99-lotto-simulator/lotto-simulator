package com.lotto.lotto_simulator.repository.lottocombinationrepository;

import com.lotto.lotto_simulator.entity.LottoCombination;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;


import static com.lotto.lotto_simulator.entity.QLottoCombination.*;

@Repository
@RequiredArgsConstructor
public class LottoCombinationRepositoryImpl implements LottoCombinationCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<LottoCombination> searchAll() {
        return queryFactory
                .select(lottoCombination)
                .from(lottoCombination)
                .fetch();

    }
}
