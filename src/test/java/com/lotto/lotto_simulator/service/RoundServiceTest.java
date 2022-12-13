//package com.lotto.lotto_simulator.service;
//
//import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
//import com.lotto.lotto_simulator.controller.responseDto.LankRoundDto;
//import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
//import com.lotto.lotto_simulator.entity.Round;
//import com.lotto.lotto_simulator.entity.RoundWinners;
//import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
//import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
//import com.lotto.lotto_simulator.repository.roundwinnersrepository.RoundWinnersRepository;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.ExecutionException;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//@SpringBootTest
//@Transactional
//@Service
//class RoundServiceTest {
//    @Mock
//    private LottoRepository lottoRepository;
//
//    @Mock
//    private RoundRepository roundRepository;
//    @Mock
//    private RoundWinnersRepository roundWinnersRepository;
//
//    @InjectMocks
//    private LottoService lottoService;
//
//    @InjectMocks
//    private RoundService roundService;
//
//    @Test
//    void lottoWinsV3() throws ExecutionException, InterruptedException {
//        //given
//        Long num = 1L;
//
//        Long previousCount = 1000L;
//
//        when(roundRepository.findByRound(num))
//                .thenReturn(Optional.ofNullable(Round.builder()
//                        .num1((byte) 10)
//                        .num2((byte) 23)
//                        .num3((byte) 29)
//                        .num4((byte) 33)
//                        .num5((byte) 37)
//                        .num6((byte) 40)
//                        .build()));
//
//
//
//        List<LottoDto> Lottos= new ArrayList<>();
//        Lottos.add(LottoDto.builder()
//                .firstNum((byte) 15)
//                .secondNum((byte) 18)
//                .thirdNum((byte) 28)
//                .fourthNum((byte) 36)
//                .fifthNum((byte) 38)
//                .sixthNum((byte) 40)
//                .build());
//
//        Lottos.add(LottoDto.builder()
//                .firstNum((byte) 5)
//                .secondNum((byte) 13)
//                .thirdNum((byte) 14)
//                .fourthNum((byte) 17)
//                .fifthNum((byte) 23)
//                .sixthNum((byte) 28)
//                .build());
//
//        roundWinnersRepository.save(RoundWinners.builder()
//                .id(num)
//                .firstRank(1L)
//                .secondRank(1L)
//                .thirdRank(2L)
//                .fourthRank(33L)
//                .fifthRank(444L)
//                .lottoCnt(Lottos.size() + previousCount)
//                .build());
//
////        roundWinnersRepository.findById(num).orElse(null);
//
//
//
//        //then
//        CompletableFuture<ResponseDto<LankRoundDto>> responseDtoCompletableFuture = roundService.lottoWinsV3(num);
//        LankRoundDto data = responseDtoCompletableFuture.get().getData();
//        assertThat(data.getFirstRank()).isEqualTo(1);

//    }
//}