package com.lotto.lotto_simulator.controller;


import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.RoundWinnersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RoundWinnersController {

    private final RoundWinnersService roundWinnersService;
    @CrossOrigin
    @GetMapping("/lottoWinsV2/{num}")
    public ResponseDto<?> lottoWin(@PathVariable Long num){
        return roundWinnersService.lottoWinsV2(num);
    }

}
