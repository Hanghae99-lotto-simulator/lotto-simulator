package com.lotto.lotto_simulator.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name="round",indexes = {
        @Index(name = "round_num_idx"
                , columnList = "num1, num2, num3, num4, num5, num6")
})
public class Round {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lottery")
    private Long id;

    @Column(nullable = false)
    private Byte bonus;

    // String? Date? 굳이 Date일 필요가 있나?
    @Column(nullable = false)
    private LocalDateTime date;

    @Column(nullable = false)
    private Byte num1;

    @Column(nullable = false)
    private Byte num2;

    @Column(nullable = false)
    private Byte num3;

    @Column(nullable = false)
    private Byte num4;

    @Column(nullable = false)
    private Byte num5;

    @Column(nullable = false)
    private Byte num6;





//    @OneToMany(mappedBy = "round",
//            cascade = CascadeType.ALL,
//            orphanRemoval = true,
//            fetch = FetchType.LAZY)
//    private List<Lotto> lottos = new ArrayList<>();

}