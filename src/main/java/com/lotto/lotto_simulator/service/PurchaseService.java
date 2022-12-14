package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.responseDto.LottosResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Lotto;;
import com.lotto.lotto_simulator.exception.CustomError;
import com.lotto.lotto_simulator.exception.CustomException;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import lombok.AllArgsConstructor;
import org.apache.catalina.Store;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class PurchaseService {

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

        List<Lotto> lottoList = new ArrayList<>();
        List<List<Byte>> responseLottoList = new ArrayList<>();

        for (int i = 0; i < nums; i++) {

            List<Byte> lotto = getLotto();
            Collections.sort(lotto);
            Lotto game = toEntity(lotto, uniqueCode);

            lottoList.add(game);
            responseLottoList.add(lotto);
        }

        // JDBC_TEMPLATE을 이용한 batch insert
        jdbcLottoRepository.batchInsertLottos(lottoList);

        LottosResponseDto lottosResponseDto = LottosResponseDto.builder()
                                              .lottoArray(responseLottoList)
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

    private Lotto toEntity(List<Byte> lottoNum, String uniqueCode){
        Lotto lotto = Lotto.builder()
                .firstNum(lottoNum.get(0))
                .secondNum(lottoNum.get(1))
                .thirdNum(lottoNum.get(2))
                .fourthNum(lottoNum.get(3))
                .fifthNum(lottoNum.get(4))
                .sixthNum(lottoNum.get(5))
                .uniqueCode(uniqueCode)
                .build();

        return lotto;
    }

}
