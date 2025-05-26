package com.kdev5.cleanpick.contract.dto.response;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ContractResponseDto {

    // Contract
    private Long contractId;
    private Long customerId;
    private Long managerId;
    private Long cleaningId;
    private LocalDateTime contractDate;
    private String address;
    private int totalPrice;
    private int totalTime;
    private boolean personal;

    // ContractDetail
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String housingType;
    private String pet;
    private String request;
    private ContractStatus status;

    // ContractOption
    private List<Long> cleaningOptionList;

    // RoutineContract
    private Long routineContractId;
    private float discountRate;
    private LocalDateTime contractStartDate;
    private int routineCount;
    private LocalDateTime startTime;
    private LocalDateTime time;
    private List<DayOfWeek> dayOfWeek;

    public ContractResponseDto(Long contractId, Long customerId, Long managerId, Long cleaningId, LocalDateTime contractDate, String address, int totalPrice, int totalTime, boolean personal, LocalDateTime checkIn, LocalDateTime checkOut, String housingType, String pet, ContractStatus status, String request, List<Long> cleaningOptionList, Long routineContractId, float discountRate, LocalDateTime contractStartDate, int routineCount, LocalDateTime startTime, LocalDateTime time, List<DayOfWeek> dayOfWeek) {
        this.contractId = contractId;
        this.customerId = customerId;
        this.managerId = managerId;
        this.cleaningId = cleaningId;
        this.contractDate = contractDate;
        this.address = address;
        this.totalPrice = totalPrice;
        this.totalTime = totalTime;
        this.personal = personal;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.housingType = housingType;
        this.pet = pet;
        this.status = status;
        this.request = request;
        this.cleaningOptionList = cleaningOptionList;
        this.routineContractId = routineContractId;
        this.discountRate = discountRate;
        this.contractStartDate = contractStartDate;
        this.routineCount = routineCount;
        this.startTime = startTime;
        this.time = time;
        this.dayOfWeek = dayOfWeek;
    }


    public static ContractResponseDto fromEntity(
            Contract contract,
            ContractDetail contractDetail,
            List<Long> cleaningOptionIds,
            RoutineContract routineContract
    ) {
        return new ContractResponseDto(
                contract.getId(),
                contract.getCustomer().getId(),
                contract.getManager() != null ? contract.getManager().getId() : null,
                contract.getCleaning().getId(),
                contract.getContractDate(),
                contract.getAddress(),
                contract.getTotalPrice(),
                contract.getTotalTime(),
                contract.isPersonal(),
                contractDetail.getCheckIn(),
                contractDetail.getCheckOut(),
                contractDetail.getHousingType(),
                contractDetail.getPet(),
                contractDetail.getStatus(),
                contractDetail.getRequest(),
                cleaningOptionIds,
                routineContract != null ? routineContract.getId() : null,
                routineContract != null ? routineContract.getDiscountRate() : 0f,
                routineContract != null ? routineContract.getContractStartDate() : null,
                routineContract != null ? routineContract.getRoutineCount() : 0,
                routineContract != null ? routineContract.getStartTime() : null,
                routineContract != null ? routineContract.getTime() : null,
                routineContract != null ? routineContract.getDayOfWeek() : null
            );

    }


}
