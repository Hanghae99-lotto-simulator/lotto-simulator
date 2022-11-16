package com.lotto.lotto_simulator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Configuration
public class Configurations {

    @Bean
    public HashMap<String, Integer> myMap(){
        HashMap<String, Integer> map = new HashMap<>();
        return map;
    }

}
