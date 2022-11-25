package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.requestDto.UniqueCodeDto;
import com.lotto.lotto_simulator.controller.responseDto.*;
import com.lotto.lotto_simulator.entity.Lotto;
import com.lotto.lotto_simulator.entity.LottoCombination;
import com.lotto.lotto_simulator.entity.Round;
import com.lotto.lotto_simulator.entity.Store;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.JdbcLottoCombinationRepository;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.LottoCombinationRepository;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.roundrepository.RoundRepository;
import com.lotto.lotto_simulator.repository.storerpository.StoreRepository;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.AllArgsConstructor;
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
    private final JdbcLottoCombinationRepository jdbcLottoCombinationRepository;

//    private final HashMap<String, Integer> myMap;

    //로또 더미데이터 nums개 만큼 자동으로 생성
    @Transactional
    public ResponseDto<?> lottoCreates(Long nums) {

        String uniqueCode = UUID.randomUUID().toString();

        // 6자리의 번호를 모아둔 로또 한 게임
        List<Byte> lotto;

        // DB에 insert 시킬 로또 list
        List<Lotto> lottos=new ArrayList<>();

        // front에 반환할 여러 개의 로또를 모아놓은 리스트
        List<List<Byte>> lottoList = new ArrayList<>();

        // 전체 로또 판매점 가져오기
        List<Store> stores = storeRepository.searchAll();


        for (int i = 0; i < nums; i++) {

            //로또 6자리 생성
            lotto = new ArrayList<>();
            do {
                byte num = (byte) ((Math.random() * 45) + 1);
                if (!lotto.contains(num)) {
                    lotto.add(num);
                }
            } while (lotto.size() < 6);


            //정렬
            Collections.sort(lotto);

            Lotto game = Lotto.builder()
                    .firstNum(lotto.get(0))
                    .secondNum(lotto.get(1))
                    .thirdNum(lotto.get(2))
                    .fourthNum(lotto.get(3))
                    .fifthNum(lotto.get(4))
                    .sixthNum(lotto.get(5))
                    .uniqueCode(uniqueCode)
                    .store(stores.get((int) (Math.random() * stores.size())))
                    .build();

            // DB애 넣을 로또 리스트
            lottos.add(game);

            // 반환할 로또 리스트
            lottoList.add(lotto);
        }

        // JDBC_TEMPLATE을 이용한 batch insert
        jdbcLottoCombinationRepository.batchInsertLottos(lottos);


        LottosResponseDto lottosResponseDto = LottosResponseDto.builder()
                .lottoArray(lottoList)
                .uniqueCode(uniqueCode)
                .build();
        return ResponseDto.success(lottosResponseDto);
    }


    // 수동으로 등록한 로또 번호 가져오기
    @Transactional
    public ResponseDto<?> lottoManual(LottoDto lottoDto) {
        List<Store> stores = storeRepository.searchAll();

        // 중복처리, 에러 처리 여기서 하기 --------------------------------------------------------  --------------

        // 로또 번호를 오름차순으로 정렬
        List<Byte> lottoNum = new ArrayList<>();
        lottoNum.add(lottoDto.getFirstNum());
        lottoNum.add(lottoDto.getSecondNum());
        lottoNum.add(lottoDto.getThirdNum());
        lottoNum.add(lottoDto.getFourthNum());
        lottoNum.add(lottoDto.getFifthNum());
        lottoNum.add(lottoDto.getSixthNum());

        Collections.sort(lottoNum);

        lottoRepository.save(Lotto.builder()
                .firstNum(lottoNum.get(0))
                .secondNum(lottoNum.get(1))
                .thirdNum(lottoNum.get(2))
                .fourthNum(lottoNum.get(3))
                .fifthNum(lottoNum.get(4))
                .sixthNum(lottoNum.get(5))
                .uniqueCode(UUID.randomUUID().toString())
                .store(stores.get((int) (Math.random() * stores.size())))
                .build());

        return ResponseDto.success(lottoDto);
        // 어떤 값이 입력 되었는지 response
    }

    //로또 당첨 확인
    @Transactional
    public ResponseDto<?> lottoWins(Long num) {
        Round round = roundRepository.findByRound(num).orElseThrow();
        System.out.println("round = " + round);
        //라운드 로또  추첨 번호
        List<Byte> rounds = new ArrayList<>();
        rounds.add(round.getNum1());
        rounds.add(round.getNum2());
        rounds.add(round.getNum3());
        rounds.add(round.getNum4());
        rounds.add(round.getNum5());
        rounds.add(round.getNum6());
//        rounds.add(round.getBonus());

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

        RankResponseDto rankResponseDto = RankResponseDto.builder()
                .firstRank(firstRank)
                .secondRank(secondRank)
                .thirdRank(thirdRank)
                .fourthRank(fourthRank)
                .fifthRank(fifthRank)
                .build();

        return ResponseDto.success(rankResponseDto);

    }

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

        // master
        @Transactional
        public ResponseDto<?> lottoCombinationCreate (Long nums){
            String uuid = UUID.randomUUID().toString();

            List<LottoCombination> combinationList = lottoCombinationRepository.searchAll();


            // 전체 로또 판매점 가져오기
            List<Store> stores = storeRepository.searchAll();

            // 로또 한 게임
            List<LottoCombination> lotto;

            // 여러 개의 로또를 모아놓은 리스트
            List<LottoResponseDto> allLottoList = new ArrayList<>();


            for (int i = 0; i < nums; i++) {

                //로또 6자리 생성
                lotto = new ArrayList<>();
                lotto.add(combinationList.get(((int) (Math.random() * combinationList.size()))));
                System.out.println("lotto = " + lotto);


                Lotto game = Lotto.builder()
                        .firstNum(lotto.get(0).getFirstNum())
                        .secondNum(lotto.get(0).getSecondNum())
                        .thirdNum(lotto.get(0).getThirdNum())
                        .fourthNum(lotto.get(0).getFourthNum())
                        .fifthNum(lotto.get(0).getFifthNum())
                        .sixthNum(lotto.get(0).getSixthNum())
                        .uniqueCode(uuid)
                        .store(stores.get((int) (Math.random() * stores.size())))
                        .build();

                lottoRepository.save(game);

                // 반환할 DTO 작성
                LottoResponseDto lottoResponseDto = LottoResponseDto.builder()
                        .firstNum(game.getFirstNum())
                        .secondNum(game.getSecondNum())
                        .thirdNum(game.getThirdNum())
                        .fourthNum(game.getFourthNum())
                        .fifthNum(game.getFifthNum())
                        .sixthNum(game.getSixthNum())
                        .uniqueCode(game.getUniqueCode())
                        .store(game.getStore())
                        .build();

                allLottoList.add(lottoResponseDto);
            }


            // 여러 개의 로또를 모아놓은 allLottoList 반환
            return ResponseDto.success(allLottoList); // 결과값이 보기 불편하게 나옴

        }
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
                    .data(round.getDate())
                    .RoundArray(rounds)
                    .firstRank(firstRank)
                    .secondRank(secondRank)
                    .thirdRank(thirdRank)
                    .fourthRank(fourthRank)
                    .fifthRank(fifthRank)
                    .build();
            return ResponseDto.success(builder);
        }
    }
