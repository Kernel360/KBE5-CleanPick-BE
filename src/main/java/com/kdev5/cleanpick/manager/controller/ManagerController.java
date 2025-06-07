package com.kdev5.cleanpick.manager.controller;

import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import com.kdev5.cleanpick.global.security.annotation.ManagerId;
import com.kdev5.cleanpick.global.security.auth.CustomUserDetails;
import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.manager.service.ManagerService;
import com.kdev5.cleanpick.manager.service.dto.request.ManagerDetailRequestDto;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerPrivateResponseDto;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/manager")
public class ManagerController {

    private final ManagerService managerService;

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<ManagerSearchResponseDto>>> search (
            @RequestParam(required = false)
            String cleaning,
            @RequestParam(required = false)
            String region,
            @RequestParam(required = false)
            String keyword,                             // 검색어
            @RequestParam(defaultValue = "RECOMMENDATION")
            SortType sortType,
            Pageable pageable
    ) {
        Page<ManagerSearchResponseDto> result = managerService.searchManagers(cleaning, region, keyword, sortType, pageable);
        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(result)));
    }

    @PostMapping
    @Operation(summary = "최초 매니저 정보 입력", description = "최초 매니저정보가 입력되는 API 이며, 이때 최초로 매니저 레코드가 데이터베이스에 INSERT 됩니다.")
    public ResponseEntity<ApiResponse<ManagerPrivateResponseDto>> enrollManager (
        @AuthenticationPrincipal CustomUserDetails customUserDetails,
        @Valid @RequestBody ManagerDetailRequestDto managerDetailRequestDto
    ) {
        System.out.println(managerDetailRequestDto.getAvailableTimes().get(0).getDayOfWeek());
        return ResponseEntity.ok(
            ApiResponse.ok(
                managerService.enrollManager(customUserDetails.getId(), managerDetailRequestDto)
            )
        );
    }

    @GetMapping
    @Operation(summary = "매니저 정보 조회", description = "매니저 정보 조회를 위한 API 입니다.")
    public ResponseEntity<ApiResponse<ManagerPrivateResponseDto>> getManager (@ManagerId Long managerId){
        return ResponseEntity.ok(
            ApiResponse.ok(managerService.getManager(managerId))
        );
    }

}
