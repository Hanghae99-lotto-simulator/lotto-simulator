package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.LankRoundDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Round;
import com.lotto.lotto_simulator.entity.RoundWinners;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import com.lotto.lotto_simulator.repository.roundwinnersrepository.RoundWinnersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class RoundWinnersService {

    private final LottoRepository lottoRepository;
    private final RoundWinnersRepository roundWinnersRepository;
    private final RoundRepository roundRepository;


    @Transactional
    public ResponseDto<?> lottoWinsV2(Long num) {

        LankRoundDto lankRoundDto;
        long previousCount = 0;
        RoundWinners roundWinners = roundWinnersRepository.findById(num).orElse(null);
        Round round = roundRepository.findByRound(num).orElseThrow();

        List<Byte> rounds = new ArrayList<>();
        rounds.add(round.getNum1());
        rounds.add(round.getNum2());
        rounds.add(round.getNum3());
        rounds.add(round.getNum4());
        rounds.add(round.getNum5());
        rounds.add(round.getNum6());

        if(roundWinners != null){
            if(roundWinners.getLottoCnt() == lottoRepository.count()){
                lankRoundDto = LankRoundDto.builder()
                        .id(round.getId())
                        .Count(roundWinners.getLottoCnt())
                        .BonusNum(round.getBonus())
                        .date(round.getDate())
                        .RoundArray(rounds)
                        .firstRank((int)(long)(roundWinners.getFirstRank()))
                        .secondRank((int)(long)(roundWinners.getSecondRank()))
                        .thirdRank((int)(long)(roundWinners.getThirdRank()))
                        .fourthRank((int)(long)(roundWinners.getFourthRank()))
                        .fifthRank((int)(long)(roundWinners.getFifthRank()))
                        .build();
                return ResponseDto.success(lankRoundDto);
            }
            previousCount = roundWinners.getLottoCnt();
        }



        //라운드 로또  추첨 번호


        List<LottoDto> lottos = lottoRepository.improvedSearch(previousCount);
        List<List<Byte>> lottoList = new ArrayList<>();

        for (LottoDto l : lottos) {
            List<Byte> lottoNum = new ArrayList<>();
            lottoNum.add(l.getFirstNum());
            lottoNum.add(l.getSecondNum());
            lottoNum.add(l.getThirdNum());
            lottoNum.add(l.getFourthNum());
            lottoNum.add(l.getFifthNum());
            lottoNum.add(l.getSixthNum());
            lottoList.add(lottoNum);
        }

        //등수
        long firstRank = 0;
        long secondRank = 0;
        long thirdRank = 0;
        long fourthRank = 0;
        long fifthRank = 0;

        long lottoCnt = previousCount;

        for (List<Byte> l : lottoList) {

            HashMap<Byte, Integer> map = new HashMap<>();
            for (Byte value : rounds) {
                map.put(value, map.getOrDefault(value, 0) + 1);
            }
            for (Byte aByte : l) {
                map.put(aByte, map.getOrDefault(aByte, 0) - 1);
            }


            int cnt = 0;
            for (Byte key : map.keySet()) {
                if (map.get(key) > 0) {
                    cnt++;
                }
            }
            if (cnt == 0) {
                firstRank++;
                System.out.println(lottoCnt);
            } else if (cnt == 1 && l.contains(round.getBonus())) {
                secondRank++;
            } else if (cnt == 1) {
                thirdRank++;
            } else if (cnt == 2) {
                fourthRank++;
            } else if (cnt == 3) {
                fifthRank++;
            }

            lottoCnt++;
        }


        if(roundWinners != null){
            firstRank += roundWinners.getFirstRank();
            secondRank += roundWinners.getSecondRank();
            thirdRank += roundWinners.getThirdRank();
            fourthRank += roundWinners.getFourthRank();
            fifthRank += roundWinners.getFifthRank();
        }


        roundWinners = RoundWinners.builder()
                .id(num)
                .firstRank(firstRank)
                .secondRank(secondRank)
                .thirdRank(thirdRank)
                .fourthRank(fourthRank)
                .fifthRank(fifthRank)
                .lottoCnt(lottoCnt)
                .build();

        lankRoundDto = LankRoundDto.builder()
                .id(round.getId())
                .Count(lottoCnt)
                .BonusNum(round.getBonus())
                .date(round.getDate())
                .RoundArray(rounds)
                .firstRank((int)firstRank)
                .secondRank((int)secondRank)
                .thirdRank((int)thirdRank)
                .fourthRank((int)fourthRank)
                .fifthRank((int)fifthRank)
                .build();

        roundWinnersRepository.save(roundWinners);

        return ResponseDto.success(lankRoundDto);

    }
}
