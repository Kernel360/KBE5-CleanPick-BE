package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
import com.kdev5.cleanpick.contract.service.ReadNomineeService;
import com.kdev5.cleanpick.contract.service.dto.response.ReadAcceptedMatchingResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadConfirmedMatchingResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadRequestedMatchingResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching/nominee")
@Tag(name = "nominee API", description = "[매니저용] nominee status를 기반으로 매칭을 조회하는 컨트롤러")
public class NomineeController {

    private final ReadNomineeService readNomineeService;

    private static final Long managerId = 1L; // TODO

    @GetMapping("/pending")
    @Operation(summary = "매니저에게 들어온 요청 목록 조회", description = "매니저에게 들어온 모든 요청을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<ReadRequestedMatchingResponseDto>>> readRequestedMatching(
            @RequestParam(value = "isPersonal", required = false) Boolean isPersonal,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readNomineeService.readRequestedMatching(managerId, isPersonal, PageRequest.of(page, size)))));
    }

    @GetMapping("/accepted")
    @Operation(summary = "매니저가 신청한 요청 목록 조회", description = "매니저가 수락한 모든 목록을 조회합니다.")
    public ResponseEntity<ApiResponse<PageResponse<ReadAcceptedMatchingResponseDto>>> readAcceptedMatching(
            @RequestParam(value = "type", required = false) ServiceName type,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readNomineeService.readAcceptedMatching(managerId, type, PageRequest.of(page, size)))));
    }

    @GetMapping("/confirmed")
    @Operation(summary = "최종 매칭된 목록 조회", description = "매니저와 수요자 서로 accept한 목록(최종 매칭된 목록)을 조회합니다.sortType은 최근 매칭순(MATCHED), 계약일순(CONTRACT) 입니다.")
    public ResponseEntity<ApiResponse<PageResponse<ReadConfirmedMatchingResponseDto>>> readConfirmedMatching(
            @RequestParam(value = "serviceType", required = false) ServiceName serviceType,
            @RequestParam(value = "sortType") String sortType,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readNomineeService.readConfirmedMatching(managerId, serviceType, sortType, PageRequest.of(page, size)))));
    }

}
