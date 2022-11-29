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
    private final PurchaseService purchaseService;
    private final RoundService roundService;

    //로또 더미데이터 여러개
    @CrossOrigin
    @PostMapping("/lottoAutos/{nums}")
    public ResponseDto<?> lottoAutos(@PathVariable Long nums) {
        return purchaseService.lottoCreates(nums);
    }

    //당첨 번호 추첨
    @CrossOrigin
    @GetMapping("/winningNums/{num}")
    public ResponseDto<?> lottoWins(@PathVariable Long num) {
        return roundService.winningNums(num);
    }

    //로또 수동 구매
    @CrossOrigin
    @PostMapping("/lottoManuals")
    public ResponseDto<?> lottoManual(@RequestBody LottoDto lottoDto) {
        return purchaseService.lottoManual(lottoDto);
    }


    //feature/uniqueCodeSearch
    @CrossOrigin
    @GetMapping("/lottos/info/{num}")
    public ResponseDto<?> lottoInfo(@PathVariable Long num,
                                    @RequestBody UniqueCodeDto uniqueIdDto) {
        return lottoService.lottoInfo(num, uniqueIdDto);
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

    //페이징 기능 달린 로또 회차 당첨 등수.
    @CrossOrigin
    @GetMapping("/winningNum")
    public ResponseDto<?> winningNum(@PageableDefault(page = 0, size = 1) Pageable pageable) {
        return lottoService.winningNum(pageable);
    }
}
