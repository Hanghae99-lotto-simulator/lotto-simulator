package com.lotto.lotto_simulator.entity;

import lombok.*;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Subselect("SELECT lotto_id FROM lotto.lotto_combination ORDER BY rand() limit 1000")
public class LottoSubSelect {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotto_id;

    @Column(nullable = false)
    private String uniqueCode;

    @Column(nullable = false)
    private Byte firstNum;

    @Column(nullable = false)
    private Byte secondNum;

    @Column(nullable = false)
    private Byte thirdNum;

    @Column(nullable = false)
    private Byte fourthNum;

    @Column(nullable = false)
    private Byte fifthNum;

    @Column(nullable = false)
    private Byte sixthNum;
}
