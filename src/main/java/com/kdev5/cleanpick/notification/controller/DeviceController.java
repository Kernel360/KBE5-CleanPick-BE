package com.kdev5.cleanpick.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.notification.service.DeviceService;
import com.kdev5.cleanpick.notification.service.dto.request.DeviceRegisterRequestDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/device")
@Tag(name = "device API", description = "FCM 알람을 위해 토큰 정보를 관리하는 컨트롤러")
public class DeviceController {

	private final DeviceService deviceService;

	@PostMapping
	@Operation(summary = "토큰 정보 등록 및 갱신", description = "로그인 시 호출되며 토큰을 등록하거나 정보를 갱신하는 API 입니다.")
	public ResponseEntity<ApiResponse<Void>> registerOrRenew(@RequestBody @Valid DeviceRegisterRequestDto deviceRegisterRequestDto,
		@AuthenticationPrincipal CustomUserDetails customUserDetails ) {

		deviceService.registerOrUpdateDevice(deviceRegisterRequestDto, customUserDetails.getId());
		return ResponseEntity.ok(ApiResponse.ok());
	}


}
