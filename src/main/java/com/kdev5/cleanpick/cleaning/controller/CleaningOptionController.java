package com.kdev5.cleanpick.cleaning.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.kdev5.cleanpick.cleaning.service.CleaningOptionService;

import com.kdev5.cleanpick.cleaning.service.dto.response.CleaningOptionResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/option")
@Tag(name = "cleaning option API", description = "청소 옵션을 조회하는 컨트롤러")
public class CleaningOptionController {

	private final CleaningOptionService cleaningOptionService;

	@GetMapping("/{cleaningId}")
	@Operation(summary = "청소 별 옵션 조회", description = "각 청소에 대한 옵션 이름, 추가 가격, 추가 시간을 조회합니다.")
	public ResponseEntity<ApiResponse<List<CleaningOptionResponseDto>>> getCleaningOption(@PathVariable Long cleaningId) {

		return ResponseEntity.ok(ApiResponse.ok(
			cleaningOptionService.getCleaningOption(cleaningId))
		);
	}
}
