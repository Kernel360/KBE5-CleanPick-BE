package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.dto.ContractRequestDto;
import jakarta.validation.Valid;

public interface ContractService {
    ContractRequestDto createOneContract(@Valid ContractRequestDto contractDto);
    ContractRequestDto createRoutineContract(@Valid ContractRequestDto contractDto);
}
