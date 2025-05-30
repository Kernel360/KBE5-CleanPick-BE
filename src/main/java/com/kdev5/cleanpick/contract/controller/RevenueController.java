package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.service.RevenueService;
import com.kdev5.cleanpick.contract.service.dto.response.ReadMonthlyRevenueResponseDto;
import com.kdev5.cleanpick.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/revenue")
@Tag(name = "revenue API", description = "수입, 정산 관련 컨트롤러")
public class RevenueController {
    private final RevenueService revenueService;

    private final Long userId = 1L;

    @GetMapping
    @Operation(summary = "월별 수입 조회", description = "정산 상태(정산전, 정산후)별, 월별 수입을 조회합니다. yearMonth는 YYYY-MM 형식으로 보내주세요.")
    public ResponseEntity<ApiResponse<ReadMonthlyRevenueResponseDto>> readMonthlyRevenue(
            @RequestParam(value = "status", required = false) ContractStatus status,
            @RequestParam("date") YearMonth yearMonth) {

        return ResponseEntity.ok(ApiResponse.ok(revenueService.readMonthlyRevenue(userId, status, yearMonth)));
    }

    @GetMapping("/predicted")
    @Operation(summary = "이번달 예상 수입", description = "이번달 예상 수입을 조회합니다.")
    public ResponseEntity<ApiResponse<Double>> readPredictedRevenue() {
        return ResponseEntity.ok(ApiResponse.ok(revenueService.readPredictedRevenue(userId)));
    }

}
