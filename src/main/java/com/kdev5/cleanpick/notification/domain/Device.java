package com.kdev5.cleanpick.notification.domain;

import java.time.LocalDateTime;

import com.kdev5.cleanpick.global.entity.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "device")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Device extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long userId;

	@Column(nullable = false, columnDefinition = "TEXT")
	private String deviceToken;

	@Column(nullable = false, length = 10)
	private String deviceType;

	@Column(nullable = false)
	private LocalDateTime lastLoggedInAt;

	@Column(nullable = false)
	private Boolean isActive = true;

	@Builder
	public Device(Long userId, String deviceToken, String deviceType, LocalDateTime lastLoggedInAt, Boolean isActive) {
		this.userId = userId;
		this.deviceToken = deviceToken;
		this.deviceType = deviceType;
		this.lastLoggedInAt = lastLoggedInAt;
		this.isActive = isActive;
	}

	public void renewDeviceInfo(Long userId, String deviceType) {
		this.userId = userId;
		this.deviceType = deviceType;
		this.lastLoggedInAt = LocalDateTime.now();
		this.isActive = true;
	}
}