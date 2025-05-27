package com.kdev5.cleanpick.contract.dto.response;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class OneContractResponseDto {

    // Contract
    private Long contractId;
    private Long routineContractId;
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
//    private ContractStatus status;

    // ContractOption
    private List<Long> cleaningOptionList;

    public OneContractResponseDto(Long contractId, Long routineContractId, Long customerId, Long managerId, Long cleaningId, LocalDateTime contractDate, String address, int totalPrice, int totalTime, boolean personal, LocalDateTime checkIn, LocalDateTime checkOut, String housingType, String pet, String request, List<Long> cleaningOptionList) {
        this.contractId = contractId;
        this.routineContractId = routineContractId;
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
        this.request = request;
        this.cleaningOptionList = cleaningOptionList;
    }


    public static OneContractResponseDto fromEntity(
            Contract contract,
            ContractDetail contractDetail,
            List<Long> cleaningOptionIds,
            RoutineContract routineContract
    ) {
        return new OneContractResponseDto(
                contract.getId(),
                contract.getRoutineContract() != null ? contract.getRoutineContract().getId() : null,
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
                contractDetail.getRequest(),
                cleaningOptionIds
        );

    }
}
