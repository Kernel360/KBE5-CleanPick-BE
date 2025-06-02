package com.kdev5.cleanpick.user.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users")
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

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Builder
    public User(String email, String password, LoginType loginType, Role role, UserStatus userStatus) {
        this.email = email;
        this.password = password;
        this.loginType = loginType;
        this.role = role;
        this.userStatus = userStatus;
    }

    public static User forAuthentication(Long id, Role role) {
        User user = new User();
        user.id = id;
        user.role = role;
        return user;
    }

    public void activate(User user) {
        user.userStatus = UserStatus.ACTIVE;
    }

}
