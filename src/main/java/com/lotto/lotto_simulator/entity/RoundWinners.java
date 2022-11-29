package com.lotto.lotto_simulator.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoundWinners {

    @Id
    private Long id;

    @Column(nullable = false)
    private Long firstRank;

    @Column(nullable = false)
    private Long secondRank;

    @Column(nullable = false)
    private Long thirdRank;

    @Column(nullable = false)
    private Long fourthRank;

    @Column(nullable = false)
    private Long fifthRank;

    @Column(nullable = false)
    private Long lottoCnt;

}
