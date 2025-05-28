package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.service.ContractMatchingService;
import com.kdev5.cleanpick.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
public class MatchingController {
    private final ContractMatchingService contractMatchingService;

    @PatchMapping("/{contractId}/accept")
    public ResponseEntity<ApiResponse<Void>> acceptMatching(@PathVariable Long contractId) {

        Long managerId = 1L; // TODO: 추후 연결
        contractMatchingService.acceptMatching(managerId, contractId);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PatchMapping("/{contractId}/reject")
    public ResponseEntity<ApiResponse<Void>> rejectMatching(@PathVariable Long contractId) {

        Long managerId = 1L; // TODO: 추후 연결
        contractMatchingService.rejectMatching(managerId, contractId);

        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PatchMapping("/{contractId}/reject-personal")
    public ResponseEntity<ApiResponse<Void>> rejectPersonalMatchingRequest(@PathVariable("contractId") Long contractId) {
        Long managerId = 1L; //TODO: 추후 연결
        contractMatchingService.rejectPersonalMatching(managerId, contractId);
        return ResponseEntity.ok(ApiResponse.ok());
    }

    @PatchMapping("/{contractId}/accept-personal")
    public ResponseEntity<ApiResponse<Void>> acceptPersonalMatching(@PathVariable("contractId") Long contractId) {
        Long managerId = 1L; //TODO: 추후 연결
        contractMatchingService.acceptPersonalMatching(managerId, contractId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
