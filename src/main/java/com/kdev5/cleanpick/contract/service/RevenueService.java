package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.contract.service.dto.response.ReadMonthlyRevenueResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadWorkHistoryResponseDto;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.exception.ManagerNotFoundException;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final ContractRepository contractRepository;
    private final ManagerRepository managerRepository;

    private static final double CHARGE_RATE = 0.8;

    public ReadMonthlyRevenueResponseDto readMonthlyRevenue(Long userId, ContractStatus status, YearMonth yearMonth) {

        List<Contract> contracts = contractRepository.findContractsByManagerAndStatusWithinDateRange(
                findMember(userId),
                status != null ? List.of(status) : Collections.emptyList(),
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
        );

        return toRevenueResponse(contracts, yearMonth.getYear(), yearMonth.getMonthValue());
    }

    public Double readConfirmedRevenue(Long userId) {

        YearMonth yearMonth = YearMonth.from(LocalDateTime.now());


        Integer sum = contractRepository.sumMonthlyTotalPriceByManager(
                findMember(userId),
                List.of(ContractStatus.작업후, ContractStatus.정산전, ContractStatus.정산완료),
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
        );

        return CHARGE_RATE * (sum != null ? sum : 0);
    }

    public ReadMonthlyRevenueResponseDto readConfirmedRevenueList(Long userId) {

        YearMonth yearMonth = YearMonth.from(LocalDateTime.now());

        List<Contract> contracts = contractRepository.findContractsByManagerAndStatusWithinDateRange(
                findMember(userId),
                List.of(ContractStatus.작업후, ContractStatus.정산전, ContractStatus.정산완료),
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
        );

        return toRevenueResponse(contracts, yearMonth.getYear(), yearMonth.getMonthValue());
    }

    public Double readPredictedRevenue(Long userId) {

        YearMonth yearMonth = YearMonth.from(LocalDateTime.now());

        Integer sum = contractRepository.sumMonthlyTotalPriceByManager(
                findMember(userId),
                null,
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
        );

        return CHARGE_RATE * (sum != null ? sum : 0);
    }

    public ReadMonthlyRevenueResponseDto readPredictedRevenueList(Long userId) {

        YearMonth yearMonth = YearMonth.from(LocalDateTime.now());

        List<Contract> contracts = contractRepository.findContractsByManagerAndStatusWithinDateRange(
                findMember(userId),
                List.of(ContractStatus.작업전, ContractStatus.작업중),
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
        );

        return toRevenueResponse(contracts, yearMonth.getYear(), yearMonth.getMonthValue());
    }


    private Manager findMember(Long userId) {
        return managerRepository.findById(userId).orElseThrow(() -> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND));
    }

    private ReadMonthlyRevenueResponseDto toRevenueResponse(List<Contract> contracts, int year, int month) {
        List<ReadWorkHistoryResponseDto> histories = contracts.stream()
                .map(ReadWorkHistoryResponseDto::fromEntity)
                .toList();

        double total = histories.stream()
                .mapToDouble(ReadWorkHistoryResponseDto::getPrice)
                .sum();

        return ReadMonthlyRevenueResponseDto.builder()
                .year(year)
                .month(month)
                .totalPrice(total)
                .workHistoryDtos(histories)
                .build();
    }
}
