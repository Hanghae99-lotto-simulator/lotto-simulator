package com.lotto.lotto_simulator.repository.lottorepository;

import com.lotto.lotto_simulator.config.Configuration;
import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.entity.Lotto;
import com.lotto.lotto_simulator.entity.Store;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.JdbcLottoCombinationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩(컨트롤러와 서비스는 로딩되지 않음) 술러아스 테스트
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(Configuration.class)
class LottoRepositoryTest {

    @Autowired
    private LottoRepository lottoRepository;


    //@BeforeAll -> 테스트 시작전에 한번 만 실행
    @BeforeEach // 각 테스트 시작 전에 한번 씩 실행
    public void saveDummyData(){
        String uniqueCode = "e0affffe-8938-4475-a5b5-ab7f966bf442tt";
        Byte firstNum = 2;
        Byte secondNum = 5;
        Byte thirdNum = 10;
        Byte fourthNum = 13;
        Byte fifthNum = 35;
        Byte sixthNum = 44;
        Store store = Store.builder()
                .storeName("공용 더미")
                .roadAddress("공용 도로명")
                .lotAddress("공용 주소")
                .build();

        Lotto lotto = Lotto.builder()
                .uniqueCode(uniqueCode)
                .firstNum(firstNum)
                .secondNum(secondNum)
                .thirdNum(thirdNum)
                .fourthNum(fourthNum)
                .fifthNum(fifthNum)
                .sixthNum(sixthNum)
                .store(store)
                .build();

          Lotto lottoPS = lottoRepository.save(lotto);
    }

    @Test
    @DisplayName("더미데이터 save() 테스트")
    public void saveTest(){
        //System.out.println("테스트가 잘 되나?");

        /**
         * given(데이터 준비)
         */
        String uniqueCode = "e0affffe-8938-4475-a5b5-ab7f966bf442tt";
        Byte firstNum = 3;
        Byte secondNum = 6;
        Byte thirdNum = 11;
        Byte fourthNum = 23;
        Byte fifthNum = 33;
        Byte sixthNum = 44;
        Store store = Store.builder()
                .storeName("더미 가게 이름")
                .roadAddress("도로명")
                .lotAddress("주소")
                .build();

        Lotto lotto = Lotto.builder()
                .uniqueCode(uniqueCode)
                .firstNum(firstNum)
                .secondNum(secondNum)
                .thirdNum(thirdNum)
                .fourthNum(fourthNum)
                .fifthNum(fifthNum)
                .sixthNum(sixthNum)
                .store(store)
                .build();



        /**
         * when(테스트 실행)
         */

        Lotto lottoPS = lottoRepository.save(lotto);

        /**
         *  then(검증)
         */
        assertEquals(firstNum, lottoPS.getFirstNum());
        assertEquals(secondNum, lottoPS.getSecondNum());
        assertEquals(thirdNum, lottoPS.getThirdNum());
        assertEquals(fourthNum, lottoPS.getFourthNum());
        assertEquals(fifthNum, lottoPS.getFifthNum());
        assertEquals(sixthNum, lottoPS.getSixthNum());
        assertEquals(store.getId(), lottoPS.getStore().getId());

    }

    @Test
    @DisplayName("로또 search() 테스트")
    public void searchTest() {

        /**
         * given(데이터 준비)
         */
        String uniqueCode = "e0affffe-8938-4475-a5b5-ab7f966bf442tt";
        Byte firstNum = 2;
        Byte secondNum = 5;
        Byte thirdNum = 10;
        Byte fourthNum = 13;
        Byte fifthNum = 35;
        Byte sixthNum = 44;


        /**
         * when(테스트 실행)
         */

        List<LottoDto> lottoDtoList = lottoRepository.search();

        /**
         *  then(검증)
         */
        assertEquals(firstNum, lottoDtoList.get(0).getFirstNum());
        assertEquals(secondNum, lottoDtoList.get(0).getSecondNum());
        assertEquals(thirdNum, lottoDtoList.get(0).getThirdNum());
        assertEquals(fourthNum, lottoDtoList.get(0).getFourthNum());
        assertEquals(fifthNum, lottoDtoList.get(0).getFifthNum());
        assertEquals(sixthNum, lottoDtoList.get(0).getSixthNum());
    }

    @Test
    @DisplayName("로또 uniqueCodeSearch() 테스트")
    public void uniqueCodeSearchTest(){
        /**
         * given(데이터 준비)
         */

        String uniqueCode = "e0affffe-8938-4475-a5b5-ab7f966bf442tt";
        Byte firstNum = 2;
        Byte secondNum = 5;
        Byte thirdNum = 10;
        Byte fourthNum = 13;
        Byte fifthNum = 35;
        Byte sixthNum = 44;
        Store store = Store.builder()
                .storeName("더미 가게 이름")
                .roadAddress("도로명")
                .lotAddress("주소")
                .build();

        Lotto lotto = Lotto.builder()
                .uniqueCode(uniqueCode)
                .firstNum(firstNum)
                .secondNum(secondNum)
                .thirdNum(thirdNum)
                .fourthNum(fourthNum)
                .fifthNum(fifthNum)
                .sixthNum(sixthNum)
                .store(store)
                .build();

        lottoRepository.save(lotto);


        /**
         * when(테스트 실행)
         */

        List<LottoDto> lottoDtoList = lottoRepository.uniqueCodeSearch(uniqueCode);

        /**
         *  then(검증)
         */

        assertEquals(2, lottoDtoList.size());

    }

}