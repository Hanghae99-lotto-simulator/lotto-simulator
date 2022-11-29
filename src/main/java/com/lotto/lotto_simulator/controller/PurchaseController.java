package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.LottoService;
import com.lotto.lotto_simulator.service.PurchaseService;
import com.lotto.lotto_simulator.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PurchaseController {
    private final LottoService lottoService;
    private final PurchaseService purchaseService;


    //로또 더미데이터 여러개
    @CrossOrigin
    @PostMapping("/lottoAutos/{nums}")
    public ResponseDto<?> lottoAutos(@PathVariable Long nums) {
        return purchaseService.lottoCreates(nums);
    }



    //로또 수동 구매
    @CrossOrigin
    @PostMapping("/lottoManuals")
    public ResponseDto<?> lottoManual(@RequestBody LottoDto lottoDto) {
        return purchaseService.lottoManual(lottoDto);
    }

    //test
    //800만개 난수로직 에 추가 한것 테스트용
    @CrossOrigin
    @GetMapping("/lottosAutos/{nums}")
    public ResponseDto<?> lottoAutoss(@PathVariable Long nums) {
        return purchaseService.lottoCombinationCreate(nums);
    }

    //800만게 쿼리문으로 렌덤하게 찾아와서 번호 생성 테스트용
    @CrossOrigin
    @GetMapping("/lottosAutoss/{num}")
    public ResponseDto<?> lottoCombinationCreates(@PathVariable Long num) {
        return purchaseService.lottoCombinationCreates(num);
    }
}
