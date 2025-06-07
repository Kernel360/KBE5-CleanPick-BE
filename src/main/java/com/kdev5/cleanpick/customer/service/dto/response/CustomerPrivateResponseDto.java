package com.kdev5.cleanpick.customer.service.dto.response;

import com.kdev5.cleanpick.customer.domain.Customer;

import lombok.Getter;

// 민감 정보 포함 일반 응답 X - 인증 개인 응답
@Getter
public class CustomerPrivateResponseDto {

	private final String name;

	private final String phoneNumber;

	private final String profileImageUrl;

	private final String mainAddress;

	private final String subAddress;

	public CustomerPrivateResponseDto(String name, String phoneNumber, String profileImageUrl, String mainAddress,
		String subAddress) {
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.profileImageUrl = profileImageUrl;
		this.mainAddress = mainAddress;
		this.subAddress = subAddress;
	}

	public static CustomerPrivateResponseDto fromEntity(Customer customer) {
		return new CustomerPrivateResponseDto(
			customer.getName(),
			customer.getPhoneNumber(),
			customer.getProfileImageUrl(),
			customer.getMainAddress(),
			customer.getSubAddress()
		);
	}
}
