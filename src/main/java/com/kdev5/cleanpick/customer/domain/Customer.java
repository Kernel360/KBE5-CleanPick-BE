package com.kdev5.cleanpick.customer.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.customer.domain.enumeration.LoginType;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "customer")
public class Customer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

}