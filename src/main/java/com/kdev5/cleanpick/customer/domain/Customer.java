package com.kdev5.cleanpick.customer.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.customer.domain.enumeration.LoginType;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "customer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Customer extends BaseTimeEntity {

    @Id
    private Long id;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private User user;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(length = 255)
    private String mainAddress;

    @Column(length = 255)
    private String subAddress;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;


    @Builder
    public Customer(Long id, String name, String phoneNumber, String mainAddress, String subAddress,  String profileImageUrl, User user) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.profileImageUrl = profileImageUrl;
        this.user = user;
    }
}