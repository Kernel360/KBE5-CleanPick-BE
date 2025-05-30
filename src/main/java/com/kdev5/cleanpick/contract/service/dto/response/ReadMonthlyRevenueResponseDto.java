package com.kdev5.cleanpick.contract.service.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ReadMonthlyRevenueResponseDto {
    final Long totalPrice;
    final Integer year;
    final Integer month;
    final List<ReadWorkHistoryResponseDto> histories;


    @Builder
    public ReadMonthlyRevenueResponseDto(Long totalPrice, Integer year, Integer month, List<ReadWorkHistoryResponseDto> workHistoryDtos) {
        this.totalPrice = totalPrice;
        this.year = year;
        this.month = month;
        this.histories = workHistoryDtos;
    }


}
