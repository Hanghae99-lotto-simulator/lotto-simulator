package com.lotto.lotto_simulator.repository.lottocombinationrepository;

import com.lotto.lotto_simulator.controller.requestDto.LottoCombinationDto;
import com.lotto.lotto_simulator.controller.requestDto.QLottoCombinationDto;
import com.lotto.lotto_simulator.entity.LottoCombination;
import com.lotto.lotto_simulator.entity.QLotto;
import com.lotto.lotto_simulator.entity.QLottoCombination;
import com.lotto.lotto_simulator.entity.QLottoSubSelect;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.JPQLTemplates;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


import static com.lotto.lotto_simulator.entity.QLottoCombination.*;
import static com.querydsl.jpa.JPAExpressions.*;


@Repository
@RequiredArgsConstructor
public class LottoCombinationRepositoryImpl extends JPQLTemplates implements LottoCombinationCustom {
    private final JPAQueryFactory queryFactory;
    private final EntityManager entityManager;


    @Override
    public List<LottoCombination> searchAll() {
        return queryFactory
                .select(lottoCombination)
                .from(lottoCombination)
                .fetch();

    }

    @Override
    public List<LottoCombinationDto> randomNums(Long num) {
            return queryFactory
                .select(new QLottoCombinationDto(
                        lottoCombination.lotto_id,
                        lottoCombination.firstNum,
                        lottoCombination.secondNum,
                        lottoCombination.thirdNum,
                        lottoCombination.thirdNum,
                        lottoCombination.fifthNum,
                        lottoCombination.sixthNum
                ))
                .from(lottoCombination)
                .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
                .limit(num)
                .fetch();
    }

    @Override
    public List<LottoCombination> randomNum(Long num) {
        return null;
    }

//    @Override
//    public List<LottoCombination> randomNum(Long num) {
//        QLottoCombination lottoSub= new QLottoCombination("lottoSub");
//        return queryFactory
//                .selectFrom(lottoCombination)
//                .join(JPAExpressions.select(lottoSub.lotto_id)
//                        .from(lottoSub)
//                        .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
//                )
//                .on(lottoCombination.lotto_id.eq(lottoSub.lotto_id))
//                .fetch();
//    }
//    select * from lotto_combination l join (SELECT lotto_id FROM lotto_combination ORDER BY rand() limit 1000) AS r on l.lotto_id=r.lotto_id;


//    @Override
//    public List<LottoCombination> randomNum(Long num) {
//        QLottoCombination lottoSub= new QLottoCombination("lottoSub");
//        return queryFactory
//                .selectFrom(lottoCombination)
//                .join(JPAExpressions.select(lottoSub.lotto_id)
//                        .from(lottoSub)
//                        .orderBy(Expressions.numberTemplate(Double.class, "function('rand')").asc())
//                )
//                .on(lottoCombination.lotto_id.eq(lottoSub.lotto_id))
//                .fetch();
//    }
//    select * from lotto_combination l join (SELECT lotto_id FROM lotto_combination ORDER BY rand() limit 1000) AS r on l.lotto_id=r.lotto_id;


//    @Override
//    public List<LottoCombination> randomNum(Long num) {
//        QLottoSubSelect lottoSub= new QLottoSubSelect("lottoSub");
//        return queryFactory
//                .selectFrom(lottoCombination)
//                .join(lottoSub)
//                .on(lottoCombination.lotto_id.eq(lottoSub.lotto_id))
//                .fetch();
//    }
}
