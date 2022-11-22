package com.lotto.lotto_simulator.repository.lottorepository;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;

import com.lotto.lotto_simulator.controller.requestDto.QLottoDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.lotto.lotto_simulator.entity.QLotto.*;


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
}
