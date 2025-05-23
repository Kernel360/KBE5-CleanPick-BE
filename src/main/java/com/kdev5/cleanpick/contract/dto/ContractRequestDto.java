package com.kdev5.cleanpick.contract.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ContractRequestDto {

    //contract, contract_detail, contract_option, routine_contract
    //계약 정보, 계약 상세 정보, 청소 요구사항, 정기 예약 정보

    @JsonProperty("customer_id")
    private Long customerId;

    @JsonProperty("manager_id")
    private Long managerId; // nullable

    @JsonProperty("cleaning_id")
    private Long cleaningId;

    @JsonProperty("routine_contract_id")
    private Long routineContractId; // nullable

    @JsonProperty("contract_date")
    private LocalDateTime contractDate;

    @JsonProperty("address")
    private String address;

    @JsonProperty("total_price")
    private int totalPrice;

    @JsonProperty("total_time")
    private int totalTime;

    @JsonProperty("is_personal")
    private boolean personal;

    // contract_detail
    @JsonProperty("check_in")
    private LocalDateTime checkIn;

    @JsonProperty("check_out")
    private LocalDateTime checkOut;

    @JsonProperty("contract_id")
    private Long contractId;

    @JsonProperty("housing_type")
    private String housingType;

    @JsonProperty("pet")
    private String pet;

    @JsonProperty("request")
    private String request;

    @JsonProperty("status")
    private ContractStatus status;

    //contract_option
    @JsonProperty("cleaning_option_list")
    private List<Long> cleaningOptionList;

    //routine_Contract
    @JsonProperty("discount_rate")
    private float discountRate;

    @JsonProperty("contract_start_date")
    private LocalDateTime contractStartDate;

    @JsonProperty("routine_count")
    private int routineCount;

    @JsonProperty("start_time")
    private LocalDateTime startTime;

    @JsonProperty("time")
    private LocalDateTime time;

    @JsonProperty("day")
    private String day; // JSON 문자열 (예: ["MON", "WED", "FRI"])

    public ContractRequestDto(Long customerId, Long managerId, Long routineContractId, Long cleaningId, LocalDateTime contractDate, int totalPrice, String address, int totalTime, LocalDateTime checkIn, boolean personal, LocalDateTime checkOut, String housingType, Long contractId, String pet, ContractStatus status, String request, List<Long> cleaningOptionList, float discountRate, LocalDateTime contractStartDate, LocalDateTime startTime, int routineCount, LocalDateTime time, String day) {
        this.customerId = customerId;
        this.managerId = managerId;
        this.routineContractId = routineContractId;
        this.cleaningId = cleaningId;
        this.contractDate = contractDate;
        this.totalPrice = totalPrice;
        this.address = address;
        this.totalTime = totalTime;
        this.checkIn = checkIn;
        this.personal = personal;
        this.checkOut = checkOut;
        this.housingType = housingType;
        this.contractId = contractId;
        this.pet = pet;
        this.status = status;
        this.request = request;
        this.cleaningOptionList = cleaningOptionList;
        this.discountRate = discountRate;
        this.contractStartDate = contractStartDate;
        this.startTime = startTime;
        this.routineCount = routineCount;
        this.time = time;
        this.day = day;
    }

    public void setStatus(ContractStatus status) {
        this.status = status;
    }
}
