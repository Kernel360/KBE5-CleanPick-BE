package com.kdev5.cleanpick.customer.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.customer.domain.enumeration.LoginType;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "customer")
public class Customer extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(length = 255)
    private String address;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    @MapsId
    @OneToOne
    @JoinColumn(name = "id")
    private User user;

}