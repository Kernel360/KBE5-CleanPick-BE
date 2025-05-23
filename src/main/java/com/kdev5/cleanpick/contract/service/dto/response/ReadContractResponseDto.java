package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.common.util.DateTimeUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record ReadContractResponseDto(Long contractId, String serviceName, LocalDate contractDate,
                                      LocalTime contractStartTime, Integer totalTime, String address,
                                      String contractStatus, LocalDateTime createdAt) {
    @Builder
    public ReadContractResponseDto {
    }

    public static ReadContractResponseDto fromEntity(Contract contract) {

        DateTimeUtil.DateTimeParts parts = DateTimeUtil.splitDateTime(contract.getContractDate());

        return ReadContractResponseDto.builder()
                .contractId(contract.getId())
                .serviceName(contract.getCleaning().getServiceName())
                .contractDate(parts.getDate())
                .contractStartTime(parts.getTime())
                .totalTime(contract.getTotalTime())
                .address(contract.getAddress())
                .contractStatus(contract.getStatus().name())
                .createdAt(contract.getCreatedAt())
                .build();
    }
}
