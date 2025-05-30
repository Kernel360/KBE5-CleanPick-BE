package com.kdev5.cleanpick.contract.service.dto.request;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UpdateContractRequestDto {

    // request, pet, ContractDate 수정
    private String pet;
    private String request;
    private LocalDateTime contractDate;

    public UpdateContractRequestDto(String pet, String request, LocalDateTime contractDate) {
        this.pet = pet;
        this.request = request;
        this.contractDate = contractDate;
    }
}
