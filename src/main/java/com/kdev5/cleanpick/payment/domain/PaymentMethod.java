package com.kdev5.cleanpick.payment.domain;

import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.payment.domain.enumeration.PayType;
import jakarta.persistence.*;

@Entity
@Table(name = "payment_method")
public class PaymentMethod extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayType type;

    @Column(length = 255)
    private String info;

}