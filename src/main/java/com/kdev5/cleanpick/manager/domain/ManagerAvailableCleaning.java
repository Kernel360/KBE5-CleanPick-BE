package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@Entity
@Getter
@Table(name = "manager_available_cleaning")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerAvailableCleaning extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cleaning_id", nullable = false)
    private Cleaning cleaning;

    public ManagerAvailableCleaning(Manager manager, Cleaning cleaning) {
        this.manager = manager;
        this.cleaning = cleaning;
    }
}
