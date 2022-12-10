package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.LankRoundDto;
import com.lotto.lotto_simulator.controller.responseDto.RankResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Round;
import com.lotto.lotto_simulator.entity.RoundWinners;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import com.lotto.lotto_simulator.repository.roundwinnersrepository.RoundWinnersRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
@Slf4j
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
        roundAdd(round, rounds);


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
        roundAdd(round, rounds);

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


            log.info("잘 되었네요");

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


    // 로또 당첨자 수 조회 V3
    @Transactional
    @Async
    public CompletableFuture<ResponseDto<LankRoundDto>> lottoWinsV3(Long num) {

        LankRoundDto lankRoundDto;
        Long roundCount = roundRepository.countQuery();
        RoundWinners roundWinners = roundWinnersRepository.findById(num).orElse(null);
        Round round = roundRepository.findByRound(num).orElseThrow();

        long previousCount = 0;


        // 당첨번호 배열
        List<Byte> rounds = new ArrayList<>();

        roundAdd(round, rounds);

        // 기존에 조회 했었던 회차인지 확인
        if(roundWinners != null){
            // 기존에 조회했을때보다 데이터가 더 많아졌는지 확인
            CompletableFuture<ResponseDto<LankRoundDto>> lankRoundDto1 = getResponseDtoCompletableFuture(roundCount, roundWinners, round, rounds);
            if (lankRoundDto1 != null) return lankRoundDto1;
            // 기존보다 데이터가 많아졌을 경우 previousCount에 기존 데이터 수 저장
            previousCount = roundWinners.getLottoCnt();
        }

        // previousCount(기존 데이터의 수) 이후에 추가된 데이터 조회
        // previousCount가 0이라면 전부 조회
        List<LottoDto> lottoList = lottoRepository.improvedSearch(previousCount);

        // 당첨자 수 구하기
        RankResponseDto winners = lotteryDraw(lottoList, round, rounds);

        long firstRank = winners.getFirstRank();
        long secondRank = winners.getSecondRank();
        long thirdRank = winners.getThirdRank();
        long fourthRank = winners.getFourthRank();
        long fifthRank = winners.getFifthRank();


        // 기존에 당첨자 수 데이터가 있었다면 새로 계산된 부분을 갱신
        if(roundWinners != null){
            firstRank += roundWinners.getFirstRank();
            secondRank += roundWinners.getSecondRank();
            thirdRank += roundWinners.getThirdRank();
            fourthRank += roundWinners.getFourthRank();
            fifthRank += roundWinners.getFifthRank();
        }

        // 당첨자 수 데이터 저장용 DTO
        roundWinners = RoundWinners.builder()
                .id(num)
                .firstRank(firstRank)
                .secondRank(secondRank)
                .thirdRank(thirdRank)
                .fourthRank(fourthRank)
                .fifthRank(fifthRank)
                .lottoCnt(lottoList.size() + previousCount)
                .build();

        // 당첨자 수 반환용 DTO
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

        // 당첨자 수 데이터를 재사용하기 위해 저장
        roundWinnersRepository.save(roundWinners);

        return CompletableFuture.completedFuture(ResponseDto.success(lankRoundDto));
    }
    @Transactional
    public ResponseDto<LankRoundDto> lottoWinsV3T(Long num) {

        LankRoundDto lankRoundDto;
        Long roundCount = roundRepository.countQuery();
        RoundWinners roundWinners = roundWinnersRepository.findById(num).orElse(null);
        Round round = roundRepository.findByRound(num).orElseThrow();
        Long lottoCnt= lottoRepository.countQuery();
        long previousCount = 0;


        // 당첨번호 배열
        List<Byte> rounds = new ArrayList<>();

        roundAdd(round, rounds);

        // 기존에 조회 했었던 회차인지 확인
        if(roundWinners != null){
            // 기존에 조회했을때보다 데이터가 더 많아졌는지 확인
            if(roundWinners.getLottoCnt().equals(lottoCnt)){
                // 기존과 똑같을 경우 그대로 출력
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
            // 기존보다 데이터가 많아졌을 경우 previousCount에 기존 데이터 수 저장
            previousCount = roundWinners.getLottoCnt();
        }

        // previousCount(기존 데이터의 수) 이후에 추가된 데이터 조회
        // previousCount가 0이라면 전부 조회
        List<LottoDto> lottoList = lottoRepository.improvedSearch(previousCount);

        // 당첨자 수 구하기
        RankResponseDto winners = lotteryDraw(lottoList, round, rounds);

        long firstRank = winners.getFirstRank();
        long secondRank = winners.getSecondRank();
        long thirdRank = winners.getThirdRank();
        long fourthRank = winners.getFourthRank();
        long fifthRank = winners.getFifthRank();


        // 기존에 당첨자 수 데이터가 있었다면 새로 계산된 부분을 갱신
        if(roundWinners != null){
            firstRank += roundWinners.getFirstRank();
            secondRank += roundWinners.getSecondRank();
            thirdRank += roundWinners.getThirdRank();
            fourthRank += roundWinners.getFourthRank();
            fifthRank += roundWinners.getFifthRank();
        }

        // 당첨자 수 데이터 저장용 DTO
        roundWinners = RoundWinners.builder()
                .id(num)
                .firstRank(firstRank)
                .secondRank(secondRank)
                .thirdRank(thirdRank)
                .fourthRank(fourthRank)
                .fifthRank(fifthRank)
                .lottoCnt(lottoList.size() + previousCount)
                .build();

        // 당첨자 수 반환용 DTO
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

        // 당첨자 수 데이터를 재사용하기 위해 저장
        roundWinnersRepository.save(roundWinners);

        return ResponseDto.success(lankRoundDto);
    }
    private CompletableFuture<ResponseDto<LankRoundDto>> getResponseDtoCompletableFuture(Long roundCount, RoundWinners roundWinners, Round round, List<Byte> rounds) {
        LankRoundDto lankRoundDto;
        Long lottoCnt= lottoRepository.countId().getLotto_id();
        if(roundWinners.getLottoCnt().equals(lottoCnt)){
            // 기존과 똑같을 경우 그대로 출력
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
            return CompletableFuture.completedFuture(ResponseDto.success(lankRoundDto));
        }
        return null;
    }

    private static int getScore(Round round, byte[] numbers, int score) {
        if (score == 5) {
            for (byte number : numbers){
                if (number == round.getBonus()){
                    score += 10; break;
                }
            }
        }
        return score;
    }

    public static int getScore(List<Byte> rounds, byte[] numbers, int score) {
        for (byte number : numbers){
            for (byte roundNumber : rounds) {
                if (number == roundNumber) {
                    score++; break;
                }
            }
        }
        return score;
    }


    //라운드 번호 분리
    public static void roundAdd(Round round, List<Byte> rounds) {
        rounds.add(round.getNum1());
        rounds.add(round.getNum2());
        rounds.add(round.getNum3());
        rounds.add(round.getNum4());
        rounds.add(round.getNum5());
        rounds.add(round.getNum6());
    }



    // 0시에 모든 회차 당첨자 수 계산해서 저장
    @Scheduled(cron = "0 * 0 * * *" )
    @Transactional
    public ResponseDto<?> lottoWinsAll() {

        Long roundCount = roundRepository.countQuery();
        List<LottoDto> lottoList = lottoRepository.improvedSearch(0L);
        List<Round> roundList = roundRepository.findAll();
        List<RoundWinners> roundWinnersTemp = roundWinnersRepository.findAll();
        List<RoundWinners> roundWinnersList = new ArrayList<>(roundList.size());
        List<RoundWinners> roundWinnersForSave = new ArrayList<>();
        int allLottoCount = (int)lottoRepository.count();

        for (int i = 0; i < roundList.size(); i++) {
            roundWinnersList.add(null);
        }

        for (RoundWinners roundWinners : roundWinnersTemp){
            roundWinnersList.set((int)(long)roundWinners.getId() - 1, roundWinners);
        }

        for (long i = 0; i < roundCount; i++) {
            RoundWinners roundWinner = roundWinnersList.get((int) i);
            long previousCount = 0;
            long addedCount = 0;
            long firstRank = 0;
            long secondRank = 0;
            long thirdRank = 0;
            long fourthRank = 0;
            long fifthRank = 0;

            // 당첨번호 배열
            List<Byte> rounds = new ArrayList<>();
            roundAdd(roundList.get((int) i), rounds);


            // 기존에 조회 했었던 회차인지 확인
            if (roundWinner != null) {
                // 기존에 조회했을때보다 데이터가 더 많아졌는지 확인
                if (roundWinner.getLottoCnt() == allLottoCount) {
                    // 기존과 똑같을 경우 그대로 출력
                    continue;
                }
                // 기존보다 데이터가 많아졌을 경우 previousCount에 기존 데이터 수 저장
                previousCount = roundWinner.getLottoCnt();
            }

            // 가져온 로또 더미데이터 하나를 저장하는 배열
            byte[] numbers = new byte[6];
            // 같은 숫자 갯수를 저장하기 위한 변수
            int score;

            for (int j = (int)previousCount; j < lottoList.size(); j++) {

                //초기화
                numbers[0] = lottoList.get(j).getFirstNum();
                numbers[1] = lottoList.get(j).getSecondNum();
                numbers[2] = lottoList.get(j).getThirdNum();
                numbers[3] = lottoList.get(j).getFourthNum();
                numbers[4] = lottoList.get(j).getFifthNum();
                numbers[5] = lottoList.get(j).getSixthNum();
                score = 0;

                // 같은 숫자 갯수 찾기
                score = getScore(rounds, numbers, score);
                // 2등 구하기
                score = getScore(roundList.get((int) i), numbers, score);

                // 당첨자 수 누적
                switch (score) {
                    case 3:
                        fifthRank++;
                        break;
                    case 4:
                        fourthRank++;
                        break;
                    case 5:
                        thirdRank++;
                        break;
                    case 6:
                        firstRank++;
                        break;
                    case 15:
                        secondRank++;
                        break;
                }

                addedCount++;
            }


            // 기존에 당첨자 수 데이터가 있었다면 새로 계산된 부분을 갱신
            if (roundWinner != null) {
                firstRank += roundWinner.getFirstRank();
                secondRank += roundWinner.getSecondRank();
                thirdRank += roundWinner.getThirdRank();
                fourthRank += roundWinner.getFourthRank();
                fifthRank += roundWinner.getFifthRank();
            }

            // 당첨자 수 데이터 저장용 DTO
            roundWinner = RoundWinners.builder()
                    .id(i + 1)
                    .firstRank(firstRank)
                    .secondRank(secondRank)
                    .thirdRank(thirdRank)
                    .fourthRank(fourthRank)
                    .fifthRank(fifthRank)
                    .lottoCnt(previousCount + addedCount)
                    .build();

            // 당첨자 수 데이터를 재사용하기 위해 저장
            roundWinnersForSave.add(roundWinner);
        }
        roundWinnersRepository.saveAll(roundWinnersForSave);
        return ResponseDto.success("success");
    }


    private RankResponseDto lotteryDraw(List<LottoDto> lottoList, Round round, List<Byte> rounds) {

        // 가져온 로또 더미데이터 하나를 저장하는 배열
        byte[] numbers = new byte[6];
        // 같은 숫자 갯수를 저장하기 위한 변수
        int score;
        // 등수별 인원
        int firstRank = 0;
        int secondRank = 0;
        int thirdRank = 0;
        int fourthRank = 0;
        int fifthRank = 0;


        for (LottoDto lotto : lottoList) {

            //초기화
            numbers[0] = lotto.getFirstNum();
            numbers[1] = lotto.getSecondNum();
            numbers[2] = lotto.getThirdNum();
            numbers[3] = lotto.getFourthNum();
            numbers[4] = lotto.getFifthNum();
            numbers[5] = lotto.getSixthNum();
            score = 0;

            // 같은 숫자 갯수 찾기
            score = getScore(rounds, numbers, score);
            // 2등 구하기
            score = getScore(round, numbers, score);

            // 당첨자 수 누적
            switch (score) {
                case 3: fifthRank++; break;
                case 4: fourthRank++; break;
                case 5: thirdRank++; break;
                case 6: firstRank++; break;
                case 15: secondRank++; break;
            }
        }

        return RankResponseDto.builder()
                .firstRank(firstRank)
                .secondRank(secondRank)
                .thirdRank(thirdRank)
                .fourthRank(fourthRank)
                .fifthRank(fifthRank)
                .build();
    }

}


