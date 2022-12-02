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

import java.util.*;

@Service
@AllArgsConstructor
public class RoundService {
    private final LottoRepository lottoRepository;
    private final RoundWinnersRepository roundWinnersRepository;
    private final RoundRepository roundRepository;

    //페이지 기능 뺀 로또 등수
    @Transactional
    public ResponseDto<?> winningNums(Long num) {
        Round round = roundRepository.findByRound(num).orElseThrow();
        Long roundCount = roundRepository.countQuery();

        //라운드 로또  추첨 번호
        List<Byte> rounds = new ArrayList<>();
        rounds.add(round.getNum1());
        rounds.add(round.getNum2());
        rounds.add(round.getNum3());
        rounds.add(round.getNum4());
        rounds.add(round.getNum5());
        rounds.add(round.getNum6());


        System.out.println("rounds = " + Arrays.toString(rounds.toArray()));
        List<LottoDto> lottos = lottoRepository.search();
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
        int firstRank = 0;
        int secondRank = 0;
        int thirdRank = 0;
        int fourthRank = 0;
        int fifthRank = 0;
        int lottoCnt = 0;

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
//                myMap.put(lottos.get(lottoCnt).getStore().getStoreName(), myMap.getOrDefault(lottos.get(lottoCnt).getStore().getStoreName(), 0) + 1);
            } else if (cnt == 1 && l.contains(round.getBonus())) {
                secondRank++;
//                System.out.println(" 2등 l= " +l );
            } else if (cnt == 1) {
                thirdRank++;
//                System.out.println(" 3등 l= " +l );
            } else if (cnt == 2) {
                fourthRank++;
            } else if (cnt == 3) {
                fifthRank++;
            }

            lottoCnt++;
        }
        LankRoundDto builder = LankRoundDto.builder()
                .id(round.getId())
                .Count(roundCount)
                .BonusNum(round.getBonus())
                .date(round.getDate())
                .RoundArray(rounds)
                .firstRank(firstRank)
                .secondRank(secondRank)
                .thirdRank(thirdRank)
                .fourthRank(fourthRank)
                .fifthRank(fifthRank)
                .build();
        return ResponseDto.success(builder);
    }

    @Transactional
    public ResponseDto<?> lottoWinsV2(Long num) {

        LankRoundDto lankRoundDto;
        long previousCount = 0;
        Long roundCount = roundRepository.countQuery();
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
                        .Count(roundCount)
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
                .Count(roundCount)
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

    @Transactional
    public ResponseDto<?> lottoWinsV3(Long num) {

        LankRoundDto lankRoundDto;
        Long roundCount = roundRepository.countQuery();
        RoundWinners roundWinners = roundWinnersRepository.findById(num).orElse(null);
        Round round = roundRepository.findByRound(num).orElseThrow();
        long previousCount = 0;
        long firstRank = 0;
        long secondRank = 0;
        long thirdRank = 0;
        long fourthRank = 0;
        long fifthRank = 0;

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
                        .Count(round.getId())
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


        List<LottoDto> lottoList = lottoRepository.improvedSearch(previousCount);



        for (LottoDto lotto : lottoList) {
            byte[] numbers = {
                                lotto.getFirstNum(), lotto.getSecondNum(), lotto.getThirdNum(),
                                lotto.getFourthNum(), lotto.getFifthNum(), lotto.getSixthNum()
                            };
            int score = 0;

            for (byte number : numbers){
                for (byte roundNumber : rounds) {
                    if (number == roundNumber) {
                        score++;
                    }
                }
            }

            if (score == 5) {
                for (byte number : numbers){
                    if (number == round.getBonus()){
                        score += 10;
                    }
                }
            }

            switch (score) {
                case 3: fifthRank++; break;
                case 4: fourthRank++; break;
                case 5: thirdRank++; break;
                case 6: firstRank++; break;
                case 15: secondRank++; break;
            }
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
                .lottoCnt(lottoList.size() + previousCount)
                .build();

        lankRoundDto = LankRoundDto.builder()
                .id(round.getId())
                .Count(roundCount)
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
