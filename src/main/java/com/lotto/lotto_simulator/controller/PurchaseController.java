package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.PurchaseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class PurchaseController {

    private final PurchaseService purchaseService;


    //로또 더미데이터 여러개
    @CrossOrigin
    @PostMapping("/lottoAutos/{nums}")
    public ResponseDto<?> lottoAutos(@PathVariable Long nums) {
        return purchaseService.lottoCreates(nums);
    }


}
