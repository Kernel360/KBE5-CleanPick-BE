package com.kdev5.cleanpick.user.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String email;

    @Column(length = 255, nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(Long id, String email, String password, LoginType loginType, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.role = role;
    }
}
