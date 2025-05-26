package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.dto.response.ContractResponseDto;
import jakarta.validation.Valid;

public interface ContractService {
    ContractResponseDto createOneContract(@Valid ContractRequestDto contractDto);
    ContractRequestDto createRoutineContract(@Valid ContractRequestDto contractDto);
}
