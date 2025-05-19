package com.kdev5.cleanpick.user.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "manager_detail")
public class ManagerDetail extends BaseTimeEntity {

    @Id
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Lob
    private String profileMessage;
}