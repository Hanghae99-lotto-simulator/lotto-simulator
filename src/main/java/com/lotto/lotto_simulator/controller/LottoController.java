package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.LottoService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LottoController {
    private final LottoService lottoService;
    //로또 더미데이터 여러개
    @CrossOrigin
    @PostMapping("/lottoAutos/{nums}")
    public ResponseDto<?> lottoAutos(@PathVariable Long nums){
        return lottoService.lottoCreates(nums);
    }
    //로또 한개 자동
    @CrossOrigin
    @PostMapping("/lottoAutos")
    public ResponseDto<?> lottoAuto() {
        return lottoService.lottoCreate();
    }

    @CrossOrigin
    @PostMapping("/lottoManuals")
    public ResponseDto<?> lottoManual(@RequestBody LottoDto lottoDto){
        return lottoService.lottoManual(lottoDto);
    }

    // {num} 회차일 때 더미데이터 중 1등부터 5등까지 각각 몇명인지 반환
    @CrossOrigin
    @PostMapping("/lottoWins/{num}")
    public ResponseDto<?> lottoWin(@PathVariable Long num){
        return lottoService.lottoWins(num);
    }

    @CrossOrigin
    @GetMapping("/winningNum")
    public ResponseDto<?> winningNum(@PageableDefault(page = 0, size = 1) Pageable pageable){
        return lottoService.winningNum(pageable);
    }

    //로또 더미데이터 여러개(같은 UUID, 지점)
    @CrossOrigin
    @PostMapping("/lottoAutosName/{value}")
    public ResponseDto<?> lottoAutosName(@PathVariable Long value){
        return lottoService.lottoCreatesName(value);
    }

}
