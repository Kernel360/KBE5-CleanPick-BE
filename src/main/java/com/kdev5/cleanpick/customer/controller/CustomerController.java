package com.kdev5.cleanpick.customer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdev5.cleanpick.customer.service.CustomerService;
import com.kdev5.cleanpick.customer.service.dto.request.WriteCustomerRequestDto;
import com.kdev5.cleanpick.customer.service.dto.response.CustomerPrivateResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.security.annotation.CustomerId;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping
	public ResponseEntity<ApiResponse<CustomerPrivateResponseDto>> writeCustomer(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody WriteCustomerRequestDto writeCustomerRequestDto) {
		return ResponseEntity.ok(
			ApiResponse.ok(customerService.writeCustomer(customUserDetails.getId(), writeCustomerRequestDto))
		);
	}

	@PutMapping
	public ResponseEntity<ApiResponse<CustomerPrivateResponseDto>> editCustomer(@CustomerId Long customerId, @Valid @RequestBody WriteCustomerRequestDto writeCustomerRequestDto) {
		return ResponseEntity.ok(
			ApiResponse.ok(customerService.editCustomer(customerId, writeCustomerRequestDto))
		);
	}

	@GetMapping
	public ResponseEntity<ApiResponse<CustomerPrivateResponseDto>> getCustomer(@CustomerId Long id) {
		return ResponseEntity.ok(
			ApiResponse.ok(customerService.getCustomer(id))
		);
	}
}
