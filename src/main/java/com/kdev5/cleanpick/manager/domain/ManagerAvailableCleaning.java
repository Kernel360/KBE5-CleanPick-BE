package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "manager_available_cleaning")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ManagerAvailableCleaning extends BaseTimeEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "cleaning_id", nullable = false)
    private Cleaning cleaning;

    public ManagerAvailableCleaning(Manager manager, Cleaning cleaning) {
        this.manager = manager;
        this.cleaning = cleaning;
    }
}
