package com.lotto.lotto_simulator.controller;

import com.lotto.lotto_simulator.controller.requestDto.LottoDto;
import com.lotto.lotto_simulator.controller.requestDto.UniqueCodeDto;
import com.lotto.lotto_simulator.controller.responseDto.ResponseDto;
import com.lotto.lotto_simulator.service.LottoService;
import com.lotto.lotto_simulator.service.PurchaseService;
import com.lotto.lotto_simulator.service.RoundService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LottoController {
    private final LottoService lottoService;

    //feature/uniqueCodeSearch
    @CrossOrigin
    @GetMapping("/lottos/info")
    public ResponseDto<?> lottoInfo(@RequestParam Long num,
                                    @RequestParam String uniqueCode) {
        return lottoService.lottoInfo(num, uniqueCode);
    }
}
