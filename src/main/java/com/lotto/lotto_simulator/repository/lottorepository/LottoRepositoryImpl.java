package com.lotto.lotto_simulator.repository.lottorepository;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;

import com.lotto.lotto_simulator.controller.requestDto.LottoIdDto;
import com.lotto.lotto_simulator.controller.requestDto.QLottoDto;
import com.lotto.lotto_simulator.controller.requestDto.QLottoIdDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.lotto.lotto_simulator.entity.QLotto.*;
import static org.springframework.util.ObjectUtils.isEmpty;
import static com.lotto.lotto_simulator.entity.QRound.round;


//queryDsl 쿼리문 짜는 곳
@Repository
@RequiredArgsConstructor
public class LottoRepositoryImpl implements LottoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<LottoDto> search() {
        return  queryFactory
                .select(new QLottoDto(lotto.firstNum,
                        lotto.secondNum,
                        lotto.thirdNum,
                        lotto.fourthNum,
                        lotto.fifthNum,
                        lotto.sixthNum
                        ))
                .from(lotto)
                .fetch();
    }

    @Override
    public List<LottoDto> uniqueCodeSearch(String uniqueCode) {
        return queryFactory
                .select(new QLottoDto(lotto.firstNum,
                        lotto.secondNum,
                        lotto.thirdNum,
                        lotto.fourthNum,
                        lotto.fifthNum,
                        lotto.sixthNum
                ))
                .from(lotto)
                .where(lotto.uniqueCode.eq(uniqueCode))
                .fetch();
    }

    @Override
    public List<LottoDto> improvedSearch(Long previousCount) {
        return  queryFactory
                .select(new QLottoDto(
                        lotto.firstNum,
                        lotto.secondNum,
                        lotto.thirdNum,
                        lotto.fourthNum,
                        lotto.fifthNum,
                        lotto.sixthNum
                ))
                .from(lotto)
                .where(lotto.lotto_id.gt(previousCount))
                .fetch();
    }

    @Override
    public List<LottoDto> fullTextSearch(String uniqueCode) {

//        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
//                "function('match',{0},{1})", lotto.uniqueCode, uniqueCode);

        return queryFactory.select(new QLottoDto(
                lotto.firstNum,
                lotto.secondNum,
                lotto.thirdNum,
                lotto.fourthNum,
                lotto.fifthNum,
                lotto.sixthNum))
                .from(lotto)
                .where(getUniqueCode(uniqueCode))
                .fetch();
    }

    private BooleanExpression getUniqueCode(String uniqueCode) {
        NumberTemplate booleanTemplate = Expressions.numberTemplate(Double.class,
                "function('match',{0},{1})", lotto.uniqueCode, "+" + uniqueCode + "*");
        return isEmpty(uniqueCode) ? null : booleanTemplate.gt(0);
    }
    @Override
    public Long countQuery() {
        return queryFactory
                .select(lotto.lotto_id.count())
                .from(lotto)
                .fetchOne();
    }

    @Override
    public LottoIdDto countId() {
        return queryFactory
                .select(new QLottoIdDto(lotto.lotto_id))
                .from(lotto)
                .orderBy(lotto.lotto_id.desc())
                .limit(1)
                .fetchOne();
    }
}
