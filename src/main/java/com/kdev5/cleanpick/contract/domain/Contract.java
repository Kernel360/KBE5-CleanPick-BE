package com.kdev5.cleanpick.contract.domain;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.manager.domain.Manager;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "contract")
@Getter
@Setter
public class Contract extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    private Manager manager;

    @ManyToOne
    @JoinColumn(name = "cleaning_id", nullable = false)
    private Cleaning cleaning;

    @ManyToOne
    @JoinColumn(name = "routine_contract_id")
    private RoutineContract routineContract;

    private LocalDateTime contractDate;

    @Column(length = 255, nullable = false)
    private String address;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int totalTime;

    @Column(name="is_personal", nullable = false)
    private boolean personal;

}
