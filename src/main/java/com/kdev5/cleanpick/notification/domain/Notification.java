package com.kdev5.cleanpick.notification.domain;


import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.notification.domain.enumeration.NotificationType;
import jakarta.persistence.*;

@Entity
@Table(name = "notification")
public class Notification extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = true)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = true)
    private Manager manager;

    @Column(name="message", nullable = false)
    private String message;

    @Column(name = "is_read")
    private boolean isRead;

    @Enumerated(EnumType.STRING)
    private NotificationType type;
}