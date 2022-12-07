package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.LottoResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.LottosResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Lotto;
import com.lotto.lotto_simulator.entity.LottoCombination;
import com.lotto.lotto_simulator.entity.Store;
import com.lotto.lotto_simulator.exception.CustomError;
import com.lotto.lotto_simulator.exception.CustomException;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.LottoCombinationRepository;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.storerpository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PurchaseService {
    private final LottoRepository lottoRepository;
    private final StoreRepository storeRepository;
    private final LottoCombinationRepository lottoCombinationRepository;
    private final JdbcLottoRepository jdbcLottoRepository;

    //로또 더미데이터 nums개 만큼 자동으로 생성 클라이언트가 로또 여러장 구매
    @Transactional
    public ResponseDto<?> lottoCreates(Long nums) {

        if(!(nums > 0)){
            throw new CustomException(CustomError.INVALID_PARAMETER);
        }

        String uniqueCode = UUID.randomUUID().toString().replace("-","");

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
        jdbcLottoRepository.batchInsertLottos(lottos);


        LottosResponseDto lottosResponseDto = LottosResponseDto.builder()
                .lottoArray(lottoList)
                .uniqueCode(uniqueCode)
                .build();
        return ResponseDto.success(lottosResponseDto);
    }
    
    // 수동으로 로또 번호 생성
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
                .uniqueCode(UUID.randomUUID().toString().replace("-",""))
                .store(stores.get((int) (Math.random() * stores.size())))
                .build());

        return ResponseDto.success(lottoDto);
        // 어떤 값이 입력 되었는지 response
    }

    //8145040개의 확률 번호로 로또 번호 생성 하는 로직.
    @Transactional
//        @BatchSize(size = 1000)
    public ResponseDto<?> lottoCombinationCreates(Long num){
//            List<LottoCombination> combinationList = lottoCombinationRepository.searchAll();
        String uniqueCode = UUID.randomUUID().toString().replace("-","");

        List<LottoCombination> combination = lottoCombinationRepository.randomNumss(num);
        List<Store> stores = storeRepository.searchAll();
        List<Lotto> lottos=new ArrayList<>();
        List<List<Byte>> lottoList = new ArrayList<>();
//            for (int i = 0; i < num; i++) {
//
//               List<Lotto> lotto = new ArrayList<>();
//
//
//
//                Lotto game = Lotto.builder()
//                        .firstNum(combination.get(i).getFirstNum())
//                        .secondNum(combination.get(i).getSecondNum())
//                        .thirdNum(combination.get(i).getThirdNum())
//                        .fourthNum(combination.get(i).getFourthNum())
//                        .fifthNum(combination.get(i).getFifthNum())
//                        .sixthNum(combination.get(i).getSixthNum())
//                        .uniqueCode(uniqueCode)
//                        .store(stores.get((int) (Math.random() * stores.size())))
//                        .build();
//
//                // DB애 넣을 로또 리스트
//                lottos.add(game);
//
//                // 반환할 로또 리스트
////                lottoList.add(lotto);
//            }
//
//            // JDBC_TEMPLATE을 이용한 batch insert
//            jdbcLottoRepository.batchInsertLottos(lottos);
        return ResponseDto.success(combination);
    }

    // 난수 로직에서 800만개 그냥 다 불러와서 생성하는 로직
    @Transactional
    public ResponseDto<?> lottoCombinationCreate (Long nums){
        String uuid = UUID.randomUUID().toString().replace("-","");

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
}
