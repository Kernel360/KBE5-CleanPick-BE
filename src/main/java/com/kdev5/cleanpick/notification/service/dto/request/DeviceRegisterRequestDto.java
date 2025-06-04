package com.kdev5.cleanpick.notification.service.dto.request;

import java.time.LocalDateTime;

import com.kdev5.cleanpick.notification.domain.Device;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;

@Getter
public class DeviceRegisterRequestDto {

	@NotEmpty
	private final String deviceToken;

	@NotEmpty
	private final String deviceType;

	private DeviceRegisterRequestDto(String deviceToken, String deviceType) {
		this.deviceToken = deviceToken;
		this.deviceType = deviceType;
	}

	public Device toEntity(Long userId) {
		return Device.builder()
				.userId(userId)
				.deviceToken(deviceToken)
				.deviceType(deviceType)
				.lastLoggedInAt(LocalDateTime.now())
				.isActive(true)
				.build();
	}
}
