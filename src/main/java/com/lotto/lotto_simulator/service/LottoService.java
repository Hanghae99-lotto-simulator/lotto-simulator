package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoCombinationDto;
import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.requestDto.UniqueCodeDto;
import com.lotto.lotto_simulator.controller.responseDto.*;
import com.lotto.lotto_simulator.entity.Lotto;
import com.lotto.lotto_simulator.entity.LottoCombination;
import com.lotto.lotto_simulator.entity.Round;
import com.lotto.lotto_simulator.entity.Store;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.LottoCombinationRepository;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import com.lotto.lotto_simulator.repository.storerpository.StoreRepository;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.BatchSize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class LottoService {
    private final LottoRepository lottoRepository;
    private final StoreRepository storeRepository;
    private final RoundRepository roundRepository;
    private final LottoCombinationRepository lottoCombinationRepository;
    private final JdbcLottoRepository jdbcLottoRepository;



    @Transactional(readOnly = true)
    public ResponseDto<?> winningNum(Pageable pageable) {
        Page<Round> winningNum = roundRepository.pageable(pageable);
        return ResponseDto.success(winningNum);
    }

    //feature/uniqueCodeSearch
    @Transactional(readOnly = true)
    public ResponseDto<?> lottoInfo(Long num, UniqueCodeDto uniqueIdDto) {

        // 매개변수로 들어온 유니크 코드를 가지고 있는 Lotto 전부 가져오기
        List<LottoDto> lottoList = lottoRepository.uniqueCodeSearch(uniqueIdDto.getUniqueCode());

        // num라운드의 당첨번호 정보를 가져온다.
        Round round = roundRepository.findByRound(num).orElseThrow();
        List<Byte> rounds = new ArrayList<>();
        rounds.add(round.getNum1());
        rounds.add(round.getNum2());
        rounds.add(round.getNum3());
        rounds.add(round.getNum4());
        rounds.add(round.getNum5());
        rounds.add(round.getNum6());


        List<List<Byte>> singleLottoNum = new ArrayList<>();

        for (LottoDto l : lottoList) {
            List<Byte> lottoNum = new ArrayList<>();
            lottoNum.add(l.getFirstNum());
            lottoNum.add(l.getSecondNum());
            lottoNum.add(l.getThirdNum());
            lottoNum.add(l.getFourthNum());
            lottoNum.add(l.getFifthNum());
            lottoNum.add(l.getSixthNum());
            singleLottoNum.add(lottoNum);
        }

        int firstRank = 0;
        int secondRank = 0;
        int thirdRank = 0;
        int fourthRank = 0;
        int fifthRank = 0;

        // 등수와 당첨번호를 모아넣는 Map
        HashMap<Integer, List<List<Byte>>> winLottoMap = new HashMap<>();
        List<List<Byte>> firstList = new ArrayList<>();
        List<List<Byte>> secondList = new ArrayList<>();
        List<List<Byte>> thirdList = new ArrayList<>();
        List<List<Byte>> fourthList = new ArrayList<>();
        List<List<Byte>> fifthList = new ArrayList<>();

        for (List<Byte> l : singleLottoNum) {

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
                firstList.add(l);
            } else if (cnt == 1 && l.contains(round.getBonus())) {
                secondRank++;
                secondList.add(l);
            } else if (cnt == 1) {
                thirdRank++;
                thirdList.add(l);
            } else if (cnt == 2) {
                fourthRank++;
                fourthList.add(l);
            } else if (cnt == 3) {
                fifthRank++;
                fifthList.add(l);
                System.out.println("5등 = " + l);
            }

        }

        winLottoMap.put(1, firstList);
        winLottoMap.put(2, secondList);
        winLottoMap.put(3, thirdList);
        winLottoMap.put(4, fourthList);
        winLottoMap.put(5, fifthList);

//        System.out.println(winLottoMap);

//        RankResponseDto rankResponseDto = RankResponseDto.builder()
//                .firstRank(firstRank)
//                .secondRank(secondRank)
//                .thirdRank(thirdRank)
//                .fourthRank(fourthRank)
//                .fifthRank(fifthRank)
//                .build();

        return ResponseDto.success(winLottoMap);
        }




    }
