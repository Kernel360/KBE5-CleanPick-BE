package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.service.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.request.UpdateContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.response.OneContractResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.RoutineContractResponseDto;
import jakarta.validation.Valid;

public interface ContractService {
    OneContractResponseDto createOneContract(@Valid ContractRequestDto contractDto);
    RoutineContractResponseDto createRoutineContract(@Valid ContractRequestDto contractDto);
    void updateOneContract(@Valid UpdateContractRequestDto contractDto, Long contractId);
    void deleteOneContract(@Valid Long contractId);
}
