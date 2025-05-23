package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.service.ReadContractService;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractDetailResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contract")
public class ReadContractController {
    private final ReadContractService readContractService;

    @GetMapping("/{contractId}")
    public ResponseEntity<ApiResponse<ReadContractDetailResponseDto>> readDetailContract(@PathVariable("contractId") Long contractId) {
        return ResponseEntity.ok(ApiResponse.ok(readContractService.readContractDetail(contractId)));
    }
}
