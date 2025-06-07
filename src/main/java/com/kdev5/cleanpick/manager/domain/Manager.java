package com.kdev5.cleanpick.manager.domain;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.global.entity.BaseTimeEntity;
import com.kdev5.cleanpick.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Getter
@Table(name = "manager", indexes = @Index(name = "idx_location", columnList = "location"))
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

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @Column(name = "phone_number", length = 50, nullable = false)
    private String phoneNumber;

    @Column(name = "profile_image_url", length = 2048)
    private String profileImageUrl;

    @Column(name = "profile_message")
    private String profileMessage;


    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManagerAvailableTime> availableTimes;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ManagerAvailableCleaning> availableCleanings;

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

    public boolean isAvailableIn(LocalDateTime start, LocalDateTime end) {
        DayOfWeek day = start.getDayOfWeek();
        LocalTime startTime = start.toLocalTime();
        LocalTime endTime = end.toLocalTime();

        return availableTimes.stream()
                .filter(t -> t.getDayOfWeek() == day)
                .anyMatch(t -> !startTime.isBefore(t.getStartTime()) && !endTime.isAfter(t.getEndTime()));
    }

    public boolean supports(Cleaning cleaning) {

        return availableCleanings.stream()
                .anyMatch(ac -> ac.getCleaning().equals(cleaning));
    }
}

