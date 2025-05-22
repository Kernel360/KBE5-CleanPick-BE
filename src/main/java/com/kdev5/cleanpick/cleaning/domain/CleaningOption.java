package com.kdev5.cleanpick.cleaning.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cleaning_option")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CleaningOption extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cleaning_id", nullable = false)
    private Cleaning cleaning;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String type;

    private int extraPrice;

    private int extraDuration;

    @Builder
    public CleaningOption(Cleaning cleaning, String name, String type, int extraPrice, int extraDuration) {
        this.cleaning = cleaning;
        this.name = name;
        this.type = type;
        this.extraPrice = extraPrice;
        this.extraDuration = extraDuration;
    }

}