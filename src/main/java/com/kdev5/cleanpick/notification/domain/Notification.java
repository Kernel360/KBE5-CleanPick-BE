package com.kdev5.cleanpick.notification.domain;


import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;

@Entity
@Table(name = "notification")
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Lob
    private String message;

    @Column(name = "read_or_not")
    private boolean isRead;
}