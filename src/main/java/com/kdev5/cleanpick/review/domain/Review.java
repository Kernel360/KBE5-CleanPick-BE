package com.kdev5.cleanpick.review.domain;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;

@Entity
@Table(name = "review")
public class Review extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private User customer;

    @ManyToOne
    @JoinColumn(name = "manger_id", nullable = false)
    private User manager;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    private float rating;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Type type;

    public enum Type { manger, user }
}