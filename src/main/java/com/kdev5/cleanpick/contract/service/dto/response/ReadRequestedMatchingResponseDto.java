package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.common.util.DateTimeUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReadRequestedMatchingResponseDto {
    Long contractId;
    String serviceName;
    LocalDate contractDate;
    LocalTime contractStartTime;
    Integer totalTime;
    String address;
    Boolean isPersonal;
    Integer totalPrice;

    String customerName;
    Long customerRating;

    Long nomineeId;

    @Builder
    public ReadRequestedMatchingResponseDto(Long contractId, String serviceName, LocalDate contractDate, LocalTime contractStartTime, Integer totalTime, String address, Boolean isPersonal, Integer totalPrice, String customerName, Long customerRating, Long nomineeId) {
        this.contractId = contractId;
        this.serviceName = serviceName;
        this.contractDate = contractDate;
        this.contractStartTime = contractStartTime;
        this.totalTime = totalTime;
        this.address = address;
        this.isPersonal = isPersonal;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
        this.customerRating = customerRating;
        this.nomineeId = nomineeId;
    }

    public static ReadRequestedMatchingResponseDto fromEntity(Contract contract, Long nomineeId) {

        DateTimeUtil.DateTimeParts parts = DateTimeUtil.splitDateTime(contract.getContractDate());

        return ReadRequestedMatchingResponseDto.builder()
                .contractId(contract.getId())
                .serviceName(contract.getCleaning().getServiceName())
                .contractDate(parts.getDate())
                .contractStartTime(parts.getTime())
                .totalTime(contract.getTotalTime())
                .address(contract.getAddress())
                .customerName(contract.getCustomer().getName())
                .isPersonal(contract.isPersonal())
                .totalPrice(contract.getTotalPrice())
//                .customerRating() //TODO: 점수 연산 방법 논의 필요
                .nomineeId(nomineeId)
                .build();
    }

}
