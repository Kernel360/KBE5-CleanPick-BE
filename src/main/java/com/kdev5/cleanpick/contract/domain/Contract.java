package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
public class Contract extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Cleaning cleaning;

    @ManyToOne
    @JoinColumn(name = "routine_contract_id")
    private RoutineContract routineContract;

    private LocalDateTime contractDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;

    @Column(length = 255, nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ContractStatus status;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int totalTime;

    @Column(length = 50)
    private String pet;

    private String request;

    @Column(length = 50, nullable = false)
    private String housingType;
}
