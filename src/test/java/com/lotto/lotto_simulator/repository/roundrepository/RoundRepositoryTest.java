package com.lotto.lotto_simulator.repository.roundrepository;

import com.lotto.lotto_simulator.config.Configuration;
import com.lotto.lotto_simulator.entity.Round;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest // DB와 관련된 컴포넌트만 메모리에 로딩(컨트롤러와 서비스는 로딩되지 않음) 술러아스 테스트
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(Configuration.class)
class RoundRepositoryTest {

    @Autowired
    private RoundRepository roundRepository;

    @BeforeEach
    public void saveRoundData(){

        Long id = 1L;
        Long bonus = 39L;
        LocalDateTime date = LocalDateTime.of(2022,11, 25, 19, 40, 4, 2);
        Long num1 = 5L;
        Long num2 = 9L;
        Long num3 = 23L;
        Long num4 = 35L;
        Long num5 = 39L;
        Long num6 = 42L;

        Round round = Round.builder()
                .id(id)
                .bonus(bonus)
                .date(date)
                .num1(num1)
                .num2(num2)
                .num3(num3)
                .num4(num4)
                .num5(num5)
                .num6(num6)
                .build();

        Round roundPs = roundRepository.save(round);
    }

    @Test
    @DisplayName("roundRepositroy.findByRound() 테스트")
    @Sql("classpath:db/tableInit.sql")
    public void findByRoundTest(){

        /**
         * given(데이터 준비)
         */


        Long bonus = 39L;
        LocalDateTime date = LocalDateTime.of(2022,11, 25, 19, 40, 4, 2);
        Long num1 = 5L;
        Long num2 = 9L;
        Long num3 = 23L;
        Long num4 = 35L;
        Long num5 = 39L;
        Long num6 = 42L;

        /**
         * when(테스트 실행)
         */

        Round round = roundRepository.findByRound(1L).get();


        /**
         *  then(검증)
         */

        assertEquals(bonus, round.getBonus());
        assertEquals(date, round.getDate());
        assertEquals(num1, round.getNum1());
        assertEquals(num2, round.getNum2());
        assertEquals(num3, round.getNum3());
        assertEquals(num4, round.getNum4());
        assertEquals(num5, round.getNum5());
        assertEquals(num6, round.getNum6());


    }


}