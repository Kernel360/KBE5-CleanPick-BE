package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
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
    private int totalPrice;
    private int totalTime;
    private boolean personal;
    private ContractStatus status;
    private String address;
    private String subAddress;
    private Double longitude;
    private Double latitude;

    // ContractDetail
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private String housingType;
    private String pet;
    private String request;

    // ContractOption
    private List<Long> cleaningOptionList;

    public OneContractResponseDto(Long contractId, Long routineContractId, Long customerId, Long managerId, Long cleaningId, LocalDateTime contractDate, int totalPrice, int totalTime, boolean personal, ContractStatus status, LocalDateTime checkIn, LocalDateTime checkOut, String housingType, String pet, String request, List<Long> cleaningOptionList, Double longitude, Double latitude, String address, String subAddress) {
        this.contractId = contractId;
        this.routineContractId = routineContractId;
        this.customerId = customerId;
        this.managerId = managerId;
        this.cleaningId = cleaningId;
        this.contractDate = contractDate;
        this.totalPrice = totalPrice;
        this.totalTime = totalTime;
        this.personal = personal;
        this.status = status;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.housingType = housingType;
        this.pet = pet;
        this.request = request;
        this.cleaningOptionList = cleaningOptionList;
        this.address = address;
        this.subAddress = subAddress;
        this.longitude = longitude;
        this.latitude = latitude;
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
                contract.getTotalPrice(),
                contract.getTotalTime(),
                contract.isPersonal(),
                contract.getStatus(),
                contractDetail.getCheckIn(),
                contractDetail.getCheckOut(),
                contractDetail.getHousingType(),
                contractDetail.getPet(),
                contractDetail.getRequest(),
                cleaningOptionIds,
                contract.getLongitude(),
                contract.getLatitude(),
                contract.getAddress(),
                contract.getSubAddress()
        );

    }
}
