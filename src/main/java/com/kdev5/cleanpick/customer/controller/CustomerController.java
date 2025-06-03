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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Tag(name = "customer API", description = "고객에 대한 정보를 조회 및 상세 정보 입력용 컨트롤러")
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping
	@Operation(summary = "최초 고객 정보 입력", description = "최초 고객정보가 입력되는 API 이며, 이때 최초로 데이터베이스에 등록됩니다.")
	public ResponseEntity<ApiResponse<CustomerPrivateResponseDto>> writeCustomer(@AuthenticationPrincipal CustomUserDetails customUserDetails, @Valid @RequestBody WriteCustomerRequestDto writeCustomerRequestDto) {
		return ResponseEntity.ok(
			ApiResponse.ok(customerService.writeCustomer(customUserDetails.getId(), writeCustomerRequestDto))
		);
	}

	@PutMapping
	@Operation(summary = "고객 정보 변경", description = "최초 등록 이후 프로필 수정을 위한 API 입니다.")
	public ResponseEntity<ApiResponse<CustomerPrivateResponseDto>> editCustomer(@CustomerId Long customerId, @Valid @RequestBody WriteCustomerRequestDto writeCustomerRequestDto) {
		return ResponseEntity.ok(
			ApiResponse.ok(customerService.editCustomer(customerId, writeCustomerRequestDto))
		);
	}

	@GetMapping
	@Operation(summary = "고객 정보 조회", description = "개인 정보 조회를 위한 API 입니다. 개인정보가 있어 인증이 필요합니다.")
	public ResponseEntity<ApiResponse<CustomerPrivateResponseDto>> getCustomer(@CustomerId Long id) {
		return ResponseEntity.ok(
			ApiResponse.ok(customerService.getCustomer(id))
		);
	}
}
