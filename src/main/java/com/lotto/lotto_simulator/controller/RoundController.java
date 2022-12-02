package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.LottoService;
import com.lotto.lotto_simulator.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RoundController {
    private final LottoService lottoService;
    private final RoundService roundService;


    //당첨 번호 추첨
    @CrossOrigin
    @GetMapping("/winningNums/{num}")
    public ResponseDto<?> lottoWins(@PathVariable Long num) {
        return roundService.winningNums(num);
    }
    
    //전에 실행된 당첨 번호 추첨 데이타 + 새로추가된 추첨 데이타 합치는 것
    @CrossOrigin
    @GetMapping("/lottoWinsV2/{num}")
    public ResponseDto<?> lottoWin(@PathVariable Long num){
        return roundService.lottoWinsV2(num);
    }

    @CrossOrigin
    @GetMapping("/lottoWinsV3/{num}")
    public ResponseDto<?> lottoWin2(@PathVariable Long num){
        return roundService.lottoWinsV3(num);
    }


    //페이징 기능 달린 로또 회차 당첨 등수.
    @CrossOrigin
    @GetMapping("/winningNum")
    public ResponseDto<?> winningNum(@PageableDefault(page = 0, size = 1) Pageable pageable) {
        return lottoService.winningNum(pageable);
    }
}
