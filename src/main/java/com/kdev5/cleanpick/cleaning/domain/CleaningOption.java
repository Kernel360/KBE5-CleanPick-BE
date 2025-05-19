package com.kdev5.cleanpick.cleaning.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "cleaning_option")
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
}