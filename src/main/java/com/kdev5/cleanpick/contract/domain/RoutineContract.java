package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "routine_contract")
public class RoutineContract extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private User customer;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
}