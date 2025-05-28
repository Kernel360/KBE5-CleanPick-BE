package com.kdev5.cleanpick.contract.service.dto.response;

import com.kdev5.cleanpick.contract.domain.RoutineContract;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class RoutineContractResponseDto {

    // RoutineContract
    private Long routineContractId;
    private float discountRate;
    private LocalDateTime contractStartDate;
    private int routineCount;
    private LocalDateTime startTime;
    private LocalDateTime time;
    private List<DayOfWeek> dayOfWeek;

    // ContractList
    private final List<OneContractResponseDto> contracts;


    public RoutineContractResponseDto(Long routineContractId, float discountRate, LocalDateTime contractStartDate, int routineCount, LocalDateTime time, LocalDateTime startTime, List<DayOfWeek> dayOfWeek, List<OneContractResponseDto> contracts) {
        this.routineContractId = routineContractId;
        this.discountRate = discountRate;
        this.contractStartDate = contractStartDate;
        this.routineCount = routineCount;
        this.time = time;
        this.startTime = startTime;
        this.dayOfWeek = dayOfWeek;
        this.contracts = contracts;
    }

    public static RoutineContractResponseDto fromEntity(
            RoutineContract routineContract,
            List<OneContractResponseDto> contracts
    ) {

        return new RoutineContractResponseDto(
                routineContract.getId(),
                routineContract.getDiscountRate(),
                routineContract.getContractStartDate(),
                routineContract.getRoutineCount(),
                routineContract.getTime(),
                routineContract.getStartTime(),
                routineContract.getDayOfWeek(),
                contracts
        );
    }



}
