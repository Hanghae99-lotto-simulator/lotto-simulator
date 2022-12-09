package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.GambleDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Lotto;
import com.lotto.lotto_simulator.entity.Round;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
class LottoServiceTest {

    @Mock
    private LottoRepository lottoRepository;

    @Mock
    private RoundRepository roundRepository;

    @InjectMocks
    private LottoService lottoService;


    @Test
    @DisplayName("라운드 정보하고 유니크코드 검색 정상적으로 되는 지 확인 등수 확인")
    public void lottoInfos() {


        //given
        Long num=1L;
        String uniqueCode="d4b42907b15d43cfb0b1e635aeff4a00";


        when(roundRepository.findByRound(num))
                .thenReturn(Optional.ofNullable(Round.builder()
                        .num1((byte) 10)
                        .num2((byte) 23)
                        .num3((byte) 29)
                        .num4((byte) 33)
                        .num5((byte) 37)
                        .num6((byte) 40)
                        .build()));

        List<LottoDto> uLottos= new ArrayList<>();
        uLottos.add(LottoDto.builder()
                        .firstNum((byte) 15)
                        .secondNum((byte) 18)
                        .thirdNum((byte) 28)
                        .fourthNum((byte) 36)
                        .fifthNum((byte) 38)
                        .sixthNum((byte) 40)
                        .build());

        uLottos.add(LottoDto.builder()
                .firstNum((byte) 5)
                .secondNum((byte) 13)
                .thirdNum((byte) 14)
                .fourthNum((byte) 17)
                .fifthNum((byte) 23)
                .sixthNum((byte) 28)
                .build());

        when(lottoRepository.fullTextSearch(uniqueCode))
                .thenReturn(uLottos);




        //when
        Byte RoundNum = roundRepository.findByRound(num).get().getNum1();

        int size = lottoRepository.fullTextSearch(uniqueCode).size();

        //then
        ResponseDto<?> responseDto = lottoService.lottoInfos(num, uniqueCode);

        GambleDto data = (GambleDto) responseDto.getData();

        assertEquals(RoundNum, (byte) 10);
        assertEquals(data.getFifthRank(),0);
        assertEquals(size,2);
    }

}