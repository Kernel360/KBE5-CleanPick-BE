package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;



@Entity
@Getter
@Table(name = "manager")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Manager extends BaseTimeEntity {

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

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    @Column(name = "profile_message")
    private String profileMessage;

    @Column(length = 255)
    private String mainAddress;

    @Column(length = 255)
    private String subAddress;


    private Double latitude;

    private Double longitude;

    @Builder
    public Manager(Long id, User user, String name, String phoneNumber, String profileImageUrl, String profileMessage,
        String mainAddress, String subAddress, Double latitude, Double longitude) {
        this.id = id;
        this.user = user;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.profileImageUrl = profileImageUrl;
        this.profileMessage = profileMessage;
        this.mainAddress = mainAddress;
        this.subAddress = subAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}

