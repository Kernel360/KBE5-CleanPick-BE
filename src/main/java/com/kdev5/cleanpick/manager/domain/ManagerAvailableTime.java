package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "manager_available_time")
public class ManagerAvailableTime extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @Column(name = "day_of_week", nullable = false)
    private int dayOfWeek;

    private LocalTime startTime;
    private LocalTime endTime;
}