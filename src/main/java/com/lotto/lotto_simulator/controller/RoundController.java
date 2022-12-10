package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.controller.responseDto.LankRoundDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.LottoService;
import com.lotto.lotto_simulator.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class RoundController {
    private final LottoService lottoService;
    private final RoundService roundService;


    //당첨 번호 추첨
    @GetMapping("/winning-nums/{num}")
    public ResponseDto<?> lottoWins(@PathVariable Long num) {
        return roundService.winningNums(num);
    }
    
    //전에 실행된 당첨 번호 추첨 데이타 + 새로추가된 추첨 데이타 합치는 것
    @GetMapping("/lotto-winsV2/{num}")
    public ResponseDto<?> lottoWin(@PathVariable Long num){
        return roundService.lottoWinsV2(num);
    }

    @GetMapping("/lotto-winsV3/{num}")
    public CompletableFuture<CompletableFuture<ResponseDto<LankRoundDto>>> lottoWin2(@PathVariable Long num){
        return CompletableFuture.completedFuture(roundService.lottoWinsV3(num));
    }
    @GetMapping("/lotto-wins-t-V3/{num}")
    public CompletableFuture<CompletableFuture<ResponseDto<LankRoundDto>>> lottoWinT2(@PathVariable Long num){
        return CompletableFuture.completedFuture(roundService.lottoWinsV3T(num));
    }

    @GetMapping("/lotto-wins-all")
    public ResponseDto<?> lottoWinAll(){
        return roundService.lottoWinsAll();
    }

    //페이징 기능 달린 로또 회차 당첨 등수.
    @GetMapping("/winning-nums")
    public ResponseDto<?> winningNum(@PageableDefault(page = 0, size = 1) Pageable pageable) {
        return lottoService.winningNum(pageable);
    }

    // 새로운 회차 당첨번호 가져오기
    @PostMapping("/newest-winning-nums")
    public ResponseDto<?> newestWinningNum() {
        return roundService.newestWinningNums();
    }
}
