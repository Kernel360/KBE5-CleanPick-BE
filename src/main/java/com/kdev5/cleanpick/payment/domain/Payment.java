package com.kdev5.cleanpick.payment.domain;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "payment")
public class Payment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "payment_method", length = 50, nullable = false)
    private String paymentMethod;

    @Column(name = "paid_amount", nullable = false)
    private int paidAmount;

    @Column(name = "discount_amount", columnDefinition = "integer default 0")
    private int discountAmount;

}