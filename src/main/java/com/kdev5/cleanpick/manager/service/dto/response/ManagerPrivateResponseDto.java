package com.kdev5.cleanpick.manager.service.dto.response;

import com.kdev5.cleanpick.manager.domain.Manager;

import lombok.Getter;

@Getter
public class ManagerPrivateResponseDto {

	private final String name;

	private final String phoneNumber;

	private final String profileImageUrl;

	private final String mainAddress;

	private final String subAddress;

	public static ManagerPrivateResponseDto fromEntity(Manager manager) {
		return new ManagerPrivateResponseDto(
			manager.getName(),
			manager.getPhoneNumber(),
			manager.getProfileImageUrl(),
			manager.getMainAddress(),
			manager.getSubAddress()
		);
	}

	public ManagerPrivateResponseDto(String name, String phoneNumber, String profileImageUrl, String mainAddress,
		String subAddress) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.profileImageUrl = profileImageUrl;
		this.mainAddress = mainAddress;
		this.subAddress = subAddress;
	}
}
