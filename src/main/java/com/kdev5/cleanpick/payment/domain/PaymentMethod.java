package com.kdev5.cleanpick.payment.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.payment.domain.enumeration.PayType;
import jakarta.persistence.*;
import com.kdev5.cleanpick.user.domain.User;

@Entity
@Table(name = "payment_method")
public class PaymentMethod extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PayType type;

    @Column(length = 255)
    private String info;

}