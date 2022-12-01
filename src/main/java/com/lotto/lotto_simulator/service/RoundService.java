package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoCombinationDto;
import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.LankRoundDto;
import com.lotto.lotto_simulator.controller.responseDto.LottosResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.RankResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Lotto;
import com.lotto.lotto_simulator.entity.Round;
import com.lotto.lotto_simulator.entity.RoundWinners;
import com.lotto.lotto_simulator.entity.Store;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.LottoCombinationRepository;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import com.lotto.lotto_simulator.repository.roundwinnersrepository.RoundWinnersRepository;
import com.lotto.lotto_simulator.repository.storerpository.StoreRepository;
import lombok.AllArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Scheduled(cron = "* * 0 * * 7" )
    public ResponseDto<?> newestWinningNums() {

        // Round 테이블의 데이터 개수 반환
        long num = roundRepository.countQuery() + 1;

        // api에 받아올 원본 데이터 담을 변수
        String result = " ";

        try{

            // URL 객체를 통해서 url을 연결, API 주소에에매개변수로 들어온 num(회차) 추가
            String apiUrl = "https://www.dhlottery.co.kr/common.do?" +
                    "method=getLottoNumber" +
                    "&drwNo=" + num;

            URL url = new URL(apiUrl);

            BufferedReader br;

            // 원본 데이터들을 모두 버퍼에 저장해서 하나의 데이터로 만듬
            br = new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8"));

            result = br.readLine();

            // 필요한 값들 parsing
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
            Byte drwtNo1 = Byte.parseByte(String.valueOf(jsonObject.get("drwtNo1")));
            Byte drwtNo2 = Byte.parseByte(String.valueOf(jsonObject.get("drwtNo2")));
            Byte drwtNo3 = Byte.parseByte(String.valueOf(jsonObject.get("drwtNo3")));
            Byte drwtNo4 = Byte.parseByte(String.valueOf(jsonObject.get("drwtNo4")));
            Byte drwtNo5 = Byte.parseByte(String.valueOf(jsonObject.get("drwtNo5")));
            Byte drwtNo6 = Byte.parseByte(String.valueOf(jsonObject.get("drwtNo6")));
            Byte bnusNo = Byte.parseByte(String.valueOf(jsonObject.get("bnusNo")));
            String date = String.valueOf(jsonObject.get("drwNoDate"));
            date += " 00:00:00.000";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);

            // parsing한 값으로 Round 객체 생상
            Round round = Round.builder()
                    .num1(drwtNo1)
                    .num2(drwtNo2)
                    .num3(drwtNo3)
                    .num4(drwtNo4)
                    .num5(drwtNo5)
                    .num6(drwtNo6)
                    .date(dateTime)
                    .bonus(bnusNo)
                    .build();

            roundRepository.save(round);

            return ResponseDto.success("success");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseDto.success("success");
    }
}


