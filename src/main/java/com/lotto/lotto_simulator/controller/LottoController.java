package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.service.LottoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LottoController {
    private final LottoService lottoService;
    //로또 더미데이터 여러개
    @PostMapping("/lottoAuto/{nums}")
    public void lottoAutos(@PathVariable Long nums){
        lottoService.lottoCreates(nums);
    }
    //로또 한개 자동
    @PostMapping("/lottoAuto/1")
    public void lottoAuto(){
        lottoService.lottoCreate();
    }

    @PostMapping("/lottoWins/{num}")
    public void lottoWin(@PathVariable Long num){
        lottoService.lottoWins(num);
    }

}
