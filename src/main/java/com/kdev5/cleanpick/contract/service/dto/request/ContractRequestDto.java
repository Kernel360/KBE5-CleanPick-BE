package com.kdev5.cleanpick.contract.service.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.ContractOption;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.manager.domain.Manager;
import lombok.Getter;

import java.time.DayOfWeek;
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

    @JsonProperty("status")
    private ContractStatus status;

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

    @JsonProperty("day_of_week")
    private List<DayOfWeek> dayOfWeek; // JSON 문자열 (예: ["MON", "WED", "FRI"])


    public Contract toEntity(Customer customer, Manager manager, Cleaning cleaning, RoutineContract routineContract) {
        return Contract.builder()
                .customer(customer)
                .manager(manager)
                .cleaning(cleaning)
                .routineContract(routineContract)
                .contractDate(contractDate)
                .address(address)
                .totalPrice(totalPrice)
                .totalTime(totalTime)
                .status(status)
                .personal(personal)
                .build();
    }

    public ContractDetail toEntity(Contract contract) {
        return ContractDetail.builder()
                .contract(contract)
                .checkIn(checkIn)
                .checkOut(checkOut)
                .pet(pet)
                .request(request)
                .housingType(housingType)
                .build();
    }

    public ContractOption toOptionEntity(Contract contract, CleaningOption cleaningOption) {
        return ContractOption.builder()
                .contract(contract)
                .cleaningOption(cleaningOption)
                .build();
    }

    public RoutineContract toEntity(){
        return RoutineContract.builder()
                .discountRate(discountRate)
                .contractStartDate(contractStartDate)
                .routineCount(routineCount)
                .startTime(startTime)
                .time(time)
                .dayOfWeek(dayOfWeek)
                .build();
    }


    public void setStatus(ContractStatus status) {
        this.status = status;
    }

    public void setContractDate(LocalDateTime contractDate) {
        this.contractDate = contractDate;
    }
}

