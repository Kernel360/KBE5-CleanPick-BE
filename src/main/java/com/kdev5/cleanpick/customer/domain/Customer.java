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
    private String mainAddress;

    @Column(length = 255)
    private String subAddress;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    private Double latitude;

    private Double longitude;

    @Builder
    public Customer(Long id, User user, String name, String phoneNumber, String mainAddress, String subAddress,
        String profileImageUrl, Double latitude, Double longitude) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.profileImageUrl = profileImageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void changeProfile(String name, String phoneNumber, String mainAddress, String subAddress, String profileImageUrl,
            Double latitude, Double longitude) {
        this.profileImageUrl = profileImageUrl;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}