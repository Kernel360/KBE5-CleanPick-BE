package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.common.util.DateTimeUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReadAcceptedMatchingResponseDto {
    Long contractId;
    String serviceName;
    LocalDate contractDate;
    LocalTime contractStartTime;
    Integer totalTime;
    String address;
    Integer totalPrice;

    String customerName;
    Long customerRating;

    Long nomineeId;
    String status;

    @Builder
    public ReadAcceptedMatchingResponseDto(Long contractId, String serviceName, LocalDate contractDate, LocalTime contractStartTime, Integer totalTime, String address, Integer totalPrice, String customerName, Long customerRating, Long nomineeId, String status) {
        this.contractId = contractId;
        this.serviceName = serviceName;
        this.contractDate = contractDate;
        this.contractStartTime = contractStartTime;
        this.totalTime = totalTime;
        this.address = address;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
        this.customerRating = customerRating;
        this.nomineeId = nomineeId;
        this.status = status;
    }

    public static ReadAcceptedMatchingResponseDto fromEntity(Contract contract, Long nomineeId) {

        DateTimeUtil.DateTimeParts parts = DateTimeUtil.splitDateTime(contract.getContractDate());
        String status = contract.getManager() == null ? "대기중" : "신청 거절됨";

        return ReadAcceptedMatchingResponseDto.builder()
                .contractId(contract.getId())
                .serviceName(String.valueOf(contract.getCleaning().getServiceName()))
                .contractDate(parts.getDate())
                .contractStartTime(parts.getTime())
                .totalTime(contract.getTotalTime())
                .address(contract.getAddress()) // 확정 전엔 대략적인 위치만 노출
                .customerName(contract.getCustomer().getName())
                .totalPrice(contract.getTotalPrice())
//                .customerRating() //TODO: 점수 연산 방법 논의 필요
                .nomineeId(nomineeId)
                .status(status)
                .build();
    }

}
