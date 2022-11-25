package com.lotto.lotto_simulator.repository.roundrepository;

import com.lotto.lotto_simulator.entity.Round;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

import static com.lotto.lotto_simulator.entity.QRound.*;


@Repository
@RequiredArgsConstructor
public class RoundRepositoryImpl implements RoundRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Round> findByRound(Long num) {
        return Optional.ofNullable(queryFactory
                .selectFrom(round)
                .where(round.id.eq(num))
                .fetchOne());
    }
//카운트 쿼리
    @Override
    public Long countQuery() {
        return queryFactory
                .select(round.count())
                .from(round)
                .fetchOne();
    }

    @Override
    public Page<Round> pageable(Pageable pageable) {
        List<Round> roundList = queryFactory
                .selectFrom(round)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery=queryFactory
                .select(round.count())
                .from(round);

        return PageableExecutionUtils.getPage(roundList, pageable,
                countQuery::fetchOne);
    }


//    private BooleanExpression geId(Long num) {
//        return null == null ? null : round.id.eq(num);
//    }
}
