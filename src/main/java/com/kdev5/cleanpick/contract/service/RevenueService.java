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

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RevenueService {
    private final ContractRepository contractRepository;
    private final ManagerRepository managerRepository;

    private static final double CHARGE_RATE = 0.8;

    public ReadMonthlyRevenueResponseDto readMonthlyRevenue(Long userId, ContractStatus status, YearMonth yearMonth) {

        List<Contract> contracts = contractRepository.findContractsWithManagerByManagerAndStatusAndContractDateBetween(
                findMember(userId),
                status,
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
        );

        List<ReadWorkHistoryResponseDto> histories = contracts.stream()
                .map(ReadWorkHistoryResponseDto::fromEntity)
                .toList();

        double total = histories.stream()
                .mapToDouble(ReadWorkHistoryResponseDto::getPrice)
                .sum();

        return ReadMonthlyRevenueResponseDto.builder()
                .totalPrice((long) total)
                .year(yearMonth.getYear())
                .month(yearMonth.getMonthValue())
                .workHistoryDtos(histories)
                .build();

    }

    public Double readPredictedRevenue(Long userId) {
        YearMonth yearMonth = YearMonth.from(LocalDate.now());

        return CHARGE_RATE * contractRepository.sumMonthlyTotalPriceByManager(
                findMember(userId),
                yearMonth.atDay(1).atStartOfDay(),
                yearMonth.atEndOfMonth().atTime(LocalTime.MAX)
        );
    }

    private Manager findMember(Long userId) {
        return managerRepository.findById(userId).orElseThrow(() -> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND));
    }
}
