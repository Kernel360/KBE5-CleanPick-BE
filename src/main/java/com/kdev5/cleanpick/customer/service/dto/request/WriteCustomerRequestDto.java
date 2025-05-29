package com.kdev5.cleanpick.customer.service.dto.request;

import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.user.domain.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WriteCustomerRequestDto {

	@NotBlank
	private String name;

	@NotBlank
	private String phoneNumber;

	@NotBlank
	private String mainAddress;

	@NotBlank
	private String subAddress;

	private String profileImageUrl;

	public Customer toEntity(User user) {
		return Customer.builder()
			.user(user)
			.name(name)
			.phoneNumber(phoneNumber)
			.mainAddress(mainAddress)
			.subAddress(subAddress)
			.profileImageUrl(profileImageUrl)
			.build();
	}
}
