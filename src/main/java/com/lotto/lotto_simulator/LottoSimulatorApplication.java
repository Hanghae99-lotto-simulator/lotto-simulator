package com.lotto.lotto_simulator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class LottoSimulatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LottoSimulatorApplication.class, args);
    }

}
