package com.kdev5.cleanpick.manager.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "manager_available_cleaning")
public class ManagerAvailableCleaning {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private Long managerId;

    @ManyToOne
    @JoinColumn(name = "cleaning_id", nullable = false)
    private Long cleaningId;
}
