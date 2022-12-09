package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.responseDto.LottosResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Lotto;;
import com.lotto.lotto_simulator.entity.Store;
import com.lotto.lotto_simulator.exception.CustomError;
import com.lotto.lotto_simulator.exception.CustomException;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import com.lotto.lotto_simulator.repository.storerpository.StoreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class PurchaseService {
    private final StoreRepository storeRepository;
    private final JdbcLottoRepository jdbcLottoRepository;

    final int NUMBER_RANGE = 45;
    final int LOTTO_SIZE = 6;

    //로또 더미데이터 nums개 만큼 자동으로 생성 클라이언트가 로또 여러장 구매
    @Transactional
    public ResponseDto<?> lottoCreates(Long nums) {


        if(!(nums > 0)){
            throw new CustomException(CustomError.INVALID_PARAMETER);
        }

        String uniqueCode = getUniqueCode(); // UUID 생성


        // DB에 insert 시킬 로또 list
        List<Lotto> lottos = new ArrayList<>();

        // front에 반환할 여러 개의 로또를 모아놓은 리스트
        List<List<Byte>> lottoList = new ArrayList<>();

        // 전체 로또 판매점 가져오기
        List<Store> stores = storeRepository.searchAll();


        for (int i = 0; i < nums; i++) {

            // 로또 번호 6개를 랜덤으로 생성하는 메서드
            List<Byte> lotto = getLotto();


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



    // 로또 랜덤 번호 생성 메서드
    private List<Byte> getLotto(){
        HashSet<Byte> lotto = new HashSet<>();
        while(!(lotto.size() == LOTTO_SIZE)){
            byte num = (byte) getRandomNum(NUMBER_RANGE);
            lotto.add(num);
        }
        return new ArrayList<>(lotto);
    }

    // UUID를 통한 랜덤 uniqueCode 생성
    private String getUniqueCode(){
        return UUID.randomUUID().toString().replace("-","");
    }

    // 난수 생성 메서드
    private int getRandomNum(int num){
        return (int) ((Math.random() * num) + 1);
    }

}
