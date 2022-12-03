package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;


class LottoServiceTest {

    @Test
    @DisplayName("메서드검증_winningNum")
    public void 메서드검증_winningNum() {

        //given
        LottoService lottoService = new LottoService(null,null);
        Pageable pageable = Pageable.ofSize(10);

        //when
        ResponseDto<?> responseDto = lottoService.winningNum(pageable);


        //then
        assertThat(responseDto.getData()).sdflksdjflksdjfklj;
    }



    @Test
    public void test() {
        //given
        LottoService lottoService = new LottoService(null,null);


        //when
        ResponseDto<?> responseDto = lottoService.lottoInfos(1000L,"hello");
        //여기서 분명히 repo 접근할떄 NPE 터진다 >> mocking 을 쓴다 목키토를 사용!!
        //요런식으로 서비스로직을 검증하는 테스트코드를 작성해주세요!


        //then
        assertThat(responseDto.getData()).sdflksdjflksdjfklj;
    }