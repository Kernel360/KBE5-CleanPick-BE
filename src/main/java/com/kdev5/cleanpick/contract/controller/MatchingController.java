package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.kdev5.cleanpick.contract.service.ContractMatchingService;
import com.kdev5.cleanpick.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {
    private final ContractMatchingService contractMatchingService;

    @PatchMapping("/{contractId}")
    public ResponseEntity<ApiResponse<Void>> updateMatchingStatus(
            @PathVariable Long contractId,
            @RequestParam MatchingStatus status) {

        Long managerId = 1L; // TODO: 추후 연결
        contractMatchingService.updateMatchingStatus(managerId, contractId, status);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PatchMapping("/{contractId}/accept-personal")
    public ResponseEntity<ApiResponse<Void>> acceptPersonalMatchingRequest(@PathVariable("contractId") Long contractId) {
        Long managerId = 1L; //TODO: 추후 연결
        contractMatchingService.matchManagerAndCustomer(managerId, contractId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
