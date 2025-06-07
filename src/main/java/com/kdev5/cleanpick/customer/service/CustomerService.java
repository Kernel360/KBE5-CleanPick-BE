package com.kdev5.cleanpick.customer.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kdev5.cleanpick.customer.domain.Customer;
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

}
