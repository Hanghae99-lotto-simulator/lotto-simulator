package com.lotto.lotto_simulator.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="lotto",indexes = {
        @Index(name = "lotto_num_idx"
                , columnList = "firstNum, secondNum, thirdNum, fourthNum, fifthNum, sixthNum")
})
public class LottoCombination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lotto_id;

    @Column(nullable = false)
    private Long firstNum;

    @Column(nullable = false)
    private Long secondNum;

    @Column(nullable = false)
    private Long thirdNum;

    @Column(nullable = false)
    private Long fourthNum;

    @Column(nullable = false)
    private Long fifthNum;

    @Column(nullable = false)
    private Long sixthNum;

}
