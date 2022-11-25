package com.lotto.lotto_simulator.repository.storerpository;

import com.lotto.lotto_simulator.config.Configuration;
import com.lotto.lotto_simulator.entity.Store;
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
@Import(Configuration.class) //
class StoreRepositoryTest {

    @Autowired
    private StoreRepository storeRepository;

    @BeforeEach // 각 테스트 시작 전에 한번 씩 실행
    public void saveStoreData(){
        String storeName = "공용 store";
        String roadAddress = "공용 도로명";
        String lotAddress = "공용 주소";

        Store store = Store.builder()
                .storeName(storeName)
                .roadAddress(roadAddress)
                .lotAddress(lotAddress)
                .build();

        Store storePs = storeRepository.save(store);

    }


    @Test
    @DisplayName("storeRepository.searchAll() 테스트")
    public void storeSearchTest(){

        /**
         * given(데이터 준비)
         */

        String storeName = "공용 store";
        String roadAddress = "공용 도로명";
        String lotAddress = "공용 주소";

        /**
         * when(테스트 실행)
         */

        List<Store> storeList = storeRepository.searchAll();

        /**
         *  then(검증)
         */

        assertEquals(storeName, storeList.get(0).getStoreName());
        assertEquals(roadAddress, storeList.get(0).getRoadAddress());
        assertEquals(lotAddress, storeList.get(0).getLotAddress());
    }


}