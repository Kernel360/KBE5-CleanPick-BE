package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.dto.response.OneContractResponseDto;
import com.kdev5.cleanpick.contract.dto.response.RoutineContractResponseDto;
import jakarta.validation.Valid;

public interface ContractService {
    OneContractResponseDto createOneContract(@Valid ContractRequestDto contractDto);
    RoutineContractResponseDto createRoutineContract(@Valid ContractRequestDto contractDto);
}
