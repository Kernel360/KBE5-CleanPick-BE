package com.kdev5.cleanpick.manager.service.dto.request;

import java.util.List;

import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.ManagerAvailableCleaning;
import com.kdev5.cleanpick.manager.service.dto.AvailableTimeDto;
import com.kdev5.cleanpick.user.domain.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ManagerDetailRequestDto {

	@NotBlank
	private String name;

	@NotBlank
	private String phoneNumber;

	@NotBlank
	private String mainAddress;

	@NotBlank
	private String subAddress;

	private String profileMessage;

	private String profileImageUrl;

	@NotNull
	private Double latitude;

	@NotNull
	private Double longitude;

	private List<Long> availableCleans;

	private List<AvailableTimeDto> availableTimes;

	public Manager toManagerEntity(User user) {
		return Manager.builder()
			.user(user)
			.name(name)
			.phoneNumber(phoneNumber)
			.profileMessage(profileMessage)
			.profileImageUrl(profileImageUrl)
			.mainAddress(mainAddress)
			.subAddress(subAddress)
			.latitude(latitude)
			.longitude(longitude)
			.build();
	}


}
