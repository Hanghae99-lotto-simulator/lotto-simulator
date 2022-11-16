package com.lotto.lotto_simulator.repository;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


//queryDsl 쿼리문 짜는 곳
@Repository
@RequiredArgsConstructor
public class LottoRepositoryImpl implements LottoRepositoryCustom {
    private final JPAQueryFactory queryFactory;


}
