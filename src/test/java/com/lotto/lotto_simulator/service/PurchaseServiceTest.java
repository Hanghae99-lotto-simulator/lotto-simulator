package com.lotto.lotto_simulator.service;

import com.lotto.lotto_simulator.controller.responseDto.LottosResponseDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.entity.Lotto;
import com.lotto.lotto_simulator.entity.Store;
import com.lotto.lotto_simulator.repository.lottocombinationrepository.LottoCombinationRepository;
import com.lotto.lotto_simulator.repository.lottorepository.JdbcLottoRepository;
import com.lotto.lotto_simulator.repository.lottorepository.LottoRepository;
import com.lotto.lotto_simulator.repository.storerpository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PurchaseServiceTest {

    @Mock
    private LottoRepository lottoRepository;
    @Mock
    private StoreRepository storeRepository;

    @Mock
    private JdbcLottoRepository jdbcLottoRepository;

    @InjectMocks
    private PurchaseService purchaseService;

    // 1개 입력 들어오면 로또 하나 만들어주고 반환
    // 테스트 할 때에 매개변수가 1이 들어올 때 어떤 반환 값이 나올지 예상하여 실제 해당 서비스를 실행했을 때 나온 반환값이랑 사이즈가 같은지 실행
    @Test
    @DisplayName("lottoCreates 테스트")
    public void lottoCreatesTest() {
        // given
        Long nums = 1L;
        String uniqueCode = "testUniqueCode";
        //List<Byte> lotto = (List<Byte>) Arrays.asList(1,2,3,4,5,6);
        // 6자리 숫자
        List<Byte> lotto = new ArrayList<>();
        lotto.add((byte)1);
        lotto.add((byte)2);
        lotto.add((byte)3);
        lotto.add((byte)4);
        lotto.add((byte)5);
        lotto.add((byte)6);

        // 프론트에 반환하기 위해 만든 로또 리스트
        List<List<Byte>> lottoList = List.of(lotto);

        Store store = Store.builder()
                .lotAddress("위치")
                .roadAddress("도로명")
                .storeName("스토어이름")
                .build();

        Lotto game = Lotto.builder()
                .firstNum(lotto.get(0))
                .secondNum(lotto.get(1))
                .thirdNum(lotto.get(2))
                .fourthNum(lotto.get(3))
                .fifthNum(lotto.get(4))
                .sixthNum(lotto.get(5))
                .uniqueCode(uniqueCode)
                .store(store)
                .build();
        List<Lotto> lottos = List.of(game);

        LottosResponseDto lottosResponseDto = LottosResponseDto.builder()
                .lottoArray(lottoList)
                .uniqueCode(uniqueCode)
                .build();

        // when
        when(storeRepository.searchAll()).thenReturn(List.of(store));
//        when(lottoRepository.saveAll(lottos)).thenReturn(Arrays.asList(lottos));


        ResponseDto<?> responseDto = purchaseService.lottoCreates(nums);

        LottosResponseDto result = (LottosResponseDto) responseDto.getData();

        // then
        assertThat(responseDto.isSuccess()).isTrue();
        assertThat(responseDto.getData()).isInstanceOf(LottosResponseDto.class);
        assertThat(result.getLottoArray().size()).isEqualTo(lottosResponseDto.getLottoArray().size());

    }

    @Test
    @DisplayName("nums가 0보다 작은 숫자가 들어왔을 때 예외처리 테스트")
    public void lottoCreatesExceptionTest()  {
        // given
        Long nums = -1L;
        int status = 406;
        String code = "M001";
        String message = "1 이상의 수만 입력할 수 있습니다.";
        Store store = Store.builder().build();


        // when
        // 첫번째 인자는 발생할 예외 클래스의 타입, 두번째 인자는 두번째 인자를 실행하여 예외가 발생할 경우 첫번째 인자와 발생된 Exception이 같은타입인지 체크합
        CustomException exception = assertThrows(CustomException.class, () -> purchaseService.lottoCreates(nums));

        // then
        assertThat(exception.getCustomError().getMessage()).isEqualTo(message);
        assertThat(exception.getCustomError().getCode()).isEqualTo(code);
        assertThat(exception.getCustomError().getStatus()).isEqualTo(status);

    }
}