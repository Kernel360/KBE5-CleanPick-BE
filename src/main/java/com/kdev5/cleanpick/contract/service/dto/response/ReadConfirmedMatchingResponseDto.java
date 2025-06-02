package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.common.util.DateTimeUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReadConfirmedMatchingResponseDto {
    Long contractId;
    String serviceName;
    LocalDate contractDate;
    LocalTime contractStartTime;
    Integer totalTime;
    String address;
    String detailAddress;
    Boolean isPersonal;
    Integer totalPrice;

    String customerName;
    Long customerRating;

    String status;

    @Builder
    public ReadConfirmedMatchingResponseDto(Long contractId, String serviceName, LocalDate contractDate, LocalTime contractStartTime, Integer totalTime, String address, Boolean isPersonal, Integer totalPrice, String customerName, Long customerRating, String detailAddress, String status) {
        this.contractId = contractId;
        this.serviceName = serviceName;
        this.contractDate = contractDate;
        this.contractStartTime = contractStartTime;
        this.totalTime = totalTime;
        this.address = address;
        this.detailAddress = detailAddress;
        this.isPersonal = isPersonal;
        this.totalPrice = totalPrice;
        this.customerName = customerName;
        this.customerRating = customerRating;
        this.status = status;
    }

    public static ReadConfirmedMatchingResponseDto fromEntity(Contract contract) {

        DateTimeUtil.DateTimeParts parts = DateTimeUtil.splitDateTime(contract.getContractDate());

        return ReadConfirmedMatchingResponseDto.builder()
                .contractId(contract.getId())
                .serviceName(contract.getCleaning().getServiceName().getDescription())
                .contractDate(parts.getDate())
                .contractStartTime(parts.getTime())
                .totalTime(contract.getTotalTime())
                .address(contract.getAddress())
//                .detailAddress(contract.getCustomer().getDetailAddress()) //TODO: dev 병합 후 추가 필요
                .customerName(contract.getCustomer().getName())
                .isPersonal(contract.isPersonal())
                .totalPrice(contract.getTotalPrice())
//                .customerRating() //TODO: 점수 연산 방법 논의 필요
                .status(contract.getStatus().name())
                .build();
    }

}
