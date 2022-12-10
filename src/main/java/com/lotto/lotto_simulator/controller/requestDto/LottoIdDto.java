package com.lotto.lotto_simulator.controller.requestDto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
@Getter
public class LottoIdDto {

    private Long lotto_id;

    @QueryProjection
    public LottoIdDto(Long lotto_id) {
        this.lotto_id = lotto_id;
    }
}
