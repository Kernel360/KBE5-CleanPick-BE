package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.common.util.DateTimeUtil;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class ReadContractDetailResponseDto {
    private final Long contractId;

    private final Long customerId;
    private final String customerName;

    private final Long managerId;
    private final String managerName;

    private final String serviceName;
    private final LocalDate contractDate;
    private final LocalTime contractStartTime;
    private final Integer totalTime;

    private final String address;
    private final Integer totalPrice;

    private final String contractStatus;
    private final String petInfo;
    private final String requestMessage;

    private final String housingType;

    List<ReadContractOptionResponseDto> options;

    @Builder
    public ReadContractDetailResponseDto(Long contractId, Long customerId, String customerName, Long managerId, String managerName, String serviceName, LocalDate contractDate, LocalTime contractStartTime, Integer totalTime, String address, Integer totalPrice, String contractStatus, String petInfo, String requestMessage, String housingType, List<ReadContractOptionResponseDto> options) {
        this.contractId = contractId;
        this.customerId = customerId;
        this.customerName = customerName;
        this.managerId = managerId;
        this.managerName = managerName;
        this.serviceName = serviceName;
        this.contractDate = contractDate;
        this.contractStartTime = contractStartTime;
        this.totalTime = totalTime;
        this.address = address;
        this.totalPrice = totalPrice;
        this.contractStatus = contractStatus;
        this.petInfo = petInfo;
        this.requestMessage = requestMessage;
        this.housingType = housingType;
        this.options = options;
    }

    public static ReadContractDetailResponseDto fromEntity(ContractDetail contractDetail, List<ReadContractOptionResponseDto> options) {

        DateTimeUtil.DateTimeParts parts = DateTimeUtil.splitDateTime(contractDetail.getContract().getContractDate());

        return ReadContractDetailResponseDto.builder()
                .contractId(contractDetail.getContract().getId())
                .customerId(contractDetail.getContract().getCustomer().getId())
                .customerName(contractDetail.getContract().getCustomer().getName())
                .managerId(contractDetail.getContract().getManager().getId())
                .managerName(contractDetail.getContract().getManager().getName())
                .serviceName(contractDetail.getContract().getCleaning().getServiceName().getDescription())
                .contractDate(parts.getDate())
                .contractStartTime(parts.getTime())
                .totalTime(contractDetail.getContract().getTotalTime())
                .address(contractDetail.getContract().getAddress()) // 확정 전엔 대략적인 위치만 노출
                .totalPrice(contractDetail.getContract().getTotalPrice())
                .contractStatus(contractDetail.getContract().getStatus().name())
                .petInfo(contractDetail.getPet())
                .requestMessage(contractDetail.getRequest())
                .housingType(contractDetail.getHousingType())
                .options(options)
                .build();
    }
}
