package com.kdev5.cleanpick.contract.service.dto.request;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.ContractOption;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.customer.domain.Customer;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ContractRequestDto {

    //contract, contract_detail, contract_option, routine_contract
    //계약 정보, 계약 상세 정보, 청소 요구사항, 정기 예약 정보

    private Long cleaningId;

    private LocalDateTime contractDate;

    private String address;

    private String subAddress;

    private Double longitude;

    private Double latitude;


    private int totalPrice;

    private int totalTime;

    private boolean personal;

    // contract_detail

    private String housingType;

    private String pet;

    private String request;


    //contract_option
    private List<Long> cleaningOptionList;

    //routine_Contract
    private float discountRate;

    private LocalDateTime contractStartDate;

    private int routineCount;

    private LocalDateTime startTime;

    private LocalDateTime time;

    private List<DayOfWeek> dayOfWeek; // JSON 문자열 (예: ["MON", "WED", "FRI"])


    public Contract toEntity(Customer customer, Cleaning cleaning, RoutineContract routineContract) {
        return Contract.builder()
                .customer(customer)
                .cleaning(cleaning)
                .routineContract(routineContract)
                .contractDate(contractDate)
                .longitude(longitude)
                .latitude(latitude)
                .address(address)
                .subAddress(subAddress)
                .totalPrice(totalPrice)
                .totalTime(totalTime)
                .status(ContractStatus.작업전)
                .personal(personal)
                .build();
    }

    public ContractDetail toEntity(Contract contract) {
        return ContractDetail.builder()
                .contract(contract)
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

    public RoutineContract toEntity() {
        return RoutineContract.builder()
                .discountRate(discountRate)
                .contractStartDate(contractStartDate)
                .routineCount(routineCount)
                .startTime(startTime)
                .time(time)
                .dayOfWeek(dayOfWeek)
                .build();
    }

    public void setContractDate(LocalDateTime contractDate) {
        this.contractDate = contractDate;
    }
}

