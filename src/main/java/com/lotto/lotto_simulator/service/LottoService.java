package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.*;
import com.lotto.lotto_simulator.entity.Round;
import com.lotto.lotto_simulator.exception.CustomError;
import com.lotto.lotto_simulator.exception.CustomException;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class LottoService {
    private final LottoRepository lottoRepository;
    private final RoundRepository roundRepository;




    @Transactional(readOnly = true)
    public ResponseDto<?> winningNum(Pageable pageable) {
        Page<Round> winningNum = roundRepository.pageable(pageable);
        return ResponseDto.success(winningNum);
    }

    //feature/uniqueCodeSearch
    @Transactional(readOnly = true)
    public ResponseDto<?> lottoInfo(Long num, String uniqueCode) {

        // 매개변수로 들어온 유니크 코드를 가지고 있는 Lotto 전부 가져오기
        List<LottoDto> lottoList = lottoRepository.uniqueCodeSearch(uniqueCode.replace(" ",""));

        // 매개변수로 들어온 유니크코드를 가지고 있는 로또 데이터가 몇 개 인지
        int totalCnt = lottoList.size();

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
        List<Byte> lottoNum=null;
        for (LottoDto l : lottoList) {
            lottoNum= new ArrayList<>();
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
        List<List<Byte>> firstList = new ArrayList<>();
        List<List<Byte>> secondList = new ArrayList<>();
        List<List<Byte>> thirdList = new ArrayList<>();
        HashMap<Byte, Integer> map=null;
        for (List<Byte> l : singleLottoNum) {
            map= new HashMap<>();
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
            } else if (cnt == 3) {
                fifthRank++;
            }
        }

        return ResponseDto.success(GambleDto.builder()
                        .firstList(firstList)
                        .secondList(secondList)
                        .thirdList(thirdList)
                        .fourthRank((long) fourthRank)
                        .fifthRank((long) fifthRank)
                        .totalCnt(totalCnt)
                        .build());
        }

    @Transactional(readOnly = true)
    public ResponseDto<?> lottoInfos(Long num, String uniqueCode) {

        // 매개변수로 들어온 유니크 코드를 가지고 있는 Lotto 전부 가져오기
        List<LottoDto> lottoList = lottoRepository.fullTextSearch(uniqueCode.replace(" ",""));

        // 매개변수로 들어온 유니크코드를 가지고 있는 로또 데이터가 몇 개 인지
        int totalCnt = lottoList.size();

        if(totalCnt == 0){
            throw new CustomException(CustomError.UNIQUE_CODE_NOT_FOUND);
        }


        // num라운드의 당첨번호 정보를 가져온다.
        Round round = roundRepository.findByRound(num).orElseThrow(() -> new CustomException(CustomError.ROUND_NOT_FOUND));
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
        List<List<Byte>> firstList = new ArrayList<>();
        List<List<Byte>> secondList = new ArrayList<>();
        List<List<Byte>> thirdList = new ArrayList<>();

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
            } else if (cnt == 3) {
                fifthRank++;
            }
        }

        return ResponseDto.success(GambleDto.builder()
                .firstList(firstList)
                .secondList(secondList)
                .thirdList(thirdList)
                .fourthRank((long) fourthRank)
                .fifthRank((long) fifthRank)
                .totalCnt(totalCnt)
                .build());
     }
    }
