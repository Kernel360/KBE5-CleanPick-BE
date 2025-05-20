package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.enumeration.LoginType;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "manager")
public class Manager extends BaseTimeEntity {

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

//    @Enumerated(EnumType.STRING)
//    @Column(nullable = false)
//    private Role role;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    @Lob
    private String profileMessage;


    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

}
