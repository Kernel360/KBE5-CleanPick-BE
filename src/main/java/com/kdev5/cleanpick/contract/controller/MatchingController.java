package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.service.ContractMatchingService;
import com.kdev5.cleanpick.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/matching")
@Tag(name = "matching API", description = "매칭 상태를 변경하는(수락/거절) 컨트롤러")
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

    @PatchMapping("/{contractId}/{managerId}")
    @Operation(summary = "최종 매칭", description = "매니저들 중 한 명을 골라 최종 매칭을 합니다.")
    public ResponseEntity<ApiResponse<Void>> confirmMatching(@PathVariable("contractId") Long contractId,
                                                             @PathVariable("managerId") Long managerId) {
        Long userId = 2L; //TODO: 추후 연결
        contractMatchingService.confirmMatching(userId, managerId, contractId);
        return ResponseEntity.ok(ApiResponse.ok());
    }
}
