package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.service.ReadContractService;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractDetailResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;
import com.kdev5.cleanpick.global.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ContractController {
    private final ReadContractService readContractService;

    @GetMapping("/{contractId}")
    public ResponseEntity<ApiResponse<ReadContractDetailResponseDto>> readDetailContract(@PathVariable("contractId") Long contractId) {
        return ResponseEntity.ok(ApiResponse.ok(readContractService.readContractDetail(contractId)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<ReadContractResponseDto>>> read(@RequestParam("status") ContractFilterStatus status, Pageable pageable) {
        return ResponseEntity.ok(ApiResponse.ok(new PageResponse<>(readContractService.readContracts(status, pageable))));
    }
}
