package com.lotto.lotto_simulator.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Lotto {
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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name="store_id")
    private Store store;

}