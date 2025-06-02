package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.common.util.DateTimeUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
public class ReadWorkHistoryResponseDto {
    Long contractId;
    LocalDate contractDate;
    String day;
    LocalTime contractStartTime;
    LocalTime contractEndTime;
    Double price;

    @Builder
    public ReadWorkHistoryResponseDto(Long contractId, LocalDate contractDate, String day, LocalTime contractStartTime, LocalTime contractEndTime, Double price) {
        this.contractId = contractId;
        this.contractDate = contractDate;
        this.contractStartTime = contractStartTime;
        this.contractEndTime = contractEndTime;
        this.price = price;
        this.day = day;
    }

    public static ReadWorkHistoryResponseDto fromEntity(Contract contract) {
        DateTimeUtil.DateTimeParts parts = DateTimeUtil.splitDateTime(contract.getContractDate());
        String day = parts.getDate().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);

        return ReadWorkHistoryResponseDto.builder()
                .contractId(contract.getId())
                .contractDate(parts.getDate())
                .day(day)
                .contractStartTime(parts.getTime())
                .contractEndTime(parts.getTime().plusHours(contract.getTotalTime()))
                .price(contract.getTotalPrice() * 0.8)
                .build();
    }
}
