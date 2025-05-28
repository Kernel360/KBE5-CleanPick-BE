package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.service.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.response.OneContractResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.RoutineContractResponseDto;
import com.kdev5.cleanpick.contract.service.ContractService;
import com.kdev5.cleanpick.contract.service.ReadContractService;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractDetailResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractController {
    private final ReadContractService readContractService;
    private final ContractService contractService;

    // 1. 청소 요청 글 작성
    // 1-1. 1회성 청소
    @PostMapping("/one")
    public ResponseEntity<ApiResponse<OneContractResponseDto>> createContract(@RequestBody @Valid ContractRequestDto contractDto) {
        OneContractResponseDto newContract = contractService.createOneContract(contractDto);
        return ResponseEntity.ok(ApiResponse.ok(newContract));
    }

    // 1-2. 정기 청소
    @PostMapping("/routine")
    public ResponseEntity<ApiResponse<RoutineContractResponseDto>> createRoutineContract(@RequestBody @Valid ContractRequestDto contractDto) {
        RoutineContractResponseDto newContracts = contractService.createRoutineContract(contractDto);
        return ResponseEntity.ok(ApiResponse.ok(newContracts));
    }

    @GetMapping("/{contractId}")
    public ResponseEntity<ApiResponse<ReadContractDetailResponseDto>> readDetailContract(@PathVariable("contractId") Long contractId) {
        return ResponseEntity.ok(ApiResponse.ok(readContractService.readContractDetail(contractId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReadContractResponseDto>>> read(@RequestParam("status") ContractFilterStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readContractService.readContracts(status, pageable))));
    }

    // Contract 수정
    @PutMapping
    public ResponseEntity<ApiResponse<Void>> changeContract(@RequestBody @Valid ContractRequestDto contractDto) {
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    //Contract 삭제
    @PostMapping
    public ResponseEntity<ApiResponse<OneContractResponseDto>> deleteContract(@RequestBody @Valid ContractRequestDto contractDto) {
        //
        return null;
    }
}



