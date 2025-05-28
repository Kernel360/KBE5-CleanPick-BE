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
@NoArgsConstructor
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
    private String address;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;


    @Builder
    public Customer(String name, String phoneNumber, String address, String profileImageUrl, User user) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.profileImageUrl = profileImageUrl;
        this.user = user;
    }

}