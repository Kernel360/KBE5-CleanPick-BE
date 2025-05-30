package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
import com.kdev5.cleanpick.contract.service.ContractService;
import com.kdev5.cleanpick.contract.service.ReadContractService;
import com.kdev5.cleanpick.contract.service.ReadNomineeService;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.contract.service.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.response.*;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
@Tag(name = "contract API", description = "계약글 CRUD 컨트롤러")
public class ContractController {

    private static final Long managerId = 1L; // TODO

    private final ReadNomineeService readNomineeService;
    private final ReadContractService readContractService;
    private final ContractService contractService;

    // 1. 청소 요청 글 작성
    // 1-1. 1회성 청소
    @PostMapping("/one")
    public ApiResponse<OneContractResponseDto> createContract(@RequestBody @Valid ContractRequestDto contractDto) {
        OneContractResponseDto newContract = contractService.createOneContract(contractDto);
        return ApiResponse.ok(newContract);
    }

    // 1-2. 정기 청소
    @PostMapping("/routine")
    public ApiResponse<RoutineContractResponseDto> createRoutineContract(@RequestBody @Valid ContractRequestDto contractDto) {
        RoutineContractResponseDto newContracts = contractService.createRoutineContract(contractDto);
        return ApiResponse.ok(newContracts);
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ApiResponse<ReadContractDetailResponseDto>> readDetailContract(@PathVariable("contractId") Long contractId) {
        return ResponseEntity.ok(ApiResponse.ok(readContractService.readContractDetail(contractId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReadContractResponseDto>>> read(@RequestParam("status") ContractFilterStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readContractService.readContracts(status, pageable))));
    }

    @PutMapping
    public ApiResponse<OneContractResponseDto> changeContract(@RequestBody @Valid ContractRequestDto contractDto) {
        return null;
    }

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



