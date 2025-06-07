package com.kdev5.cleanpick.customer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.customer.domain.exception.CustomerNotFoundException;
import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.customer.service.dto.request.WriteCustomerRequestDto;
import com.kdev5.cleanpick.customer.service.dto.response.CustomerPrivateResponseDto;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.user.domain.User;
import com.kdev5.cleanpick.user.domain.exception.UserNotFoundException;
import com.kdev5.cleanpick.user.infra.UserRepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final UserRepository userRepository;

	private final CustomerRepository customerRepository;

	@Transactional
	public CustomerPrivateResponseDto writeCustomer(Long customerId, WriteCustomerRequestDto writeCustomerRequestDto) {
		final User user = userRepository.findById(customerId).orElseThrow(
			() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

		user.activate();

		final Customer customer = customerRepository.save(writeCustomerRequestDto.toEntity(user));

		return CustomerPrivateResponseDto.fromEntity(customer);
	}

	@Transactional
	public CustomerPrivateResponseDto editCustomer(Long customerId, WriteCustomerRequestDto writeCustomerRequestDto) {

		final Customer customer = customerRepository.findById(customerId).orElseThrow(
			() -> new CustomerNotFoundException(ErrorCode.USER_NOT_FOUND));

		customer.changeProfile(
			writeCustomerRequestDto.getName(),
			writeCustomerRequestDto.getPhoneNumber(),
			writeCustomerRequestDto.getMainAddress(),
			writeCustomerRequestDto.getSubAddress(),
			writeCustomerRequestDto.getProfileImageUrl(),
			writeCustomerRequestDto.getLatitude(),
			writeCustomerRequestDto.getLongitude()
		);

		return CustomerPrivateResponseDto.fromEntity(customer);
	}

	public CustomerPrivateResponseDto getCustomer(Long customerId) {
		final Customer customer = customerRepository.findById(customerId).orElseThrow(
			() -> new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND)
		);
		return CustomerPrivateResponseDto.fromEntity(customer);
	}

}
