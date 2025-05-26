package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.dto.ContractRequestDto;
import jakarta.validation.Valid;

public interface ContractService {
    ContractRequestDto createOneContract(@Valid ContractRequestDto contractDto);
    RoutineContract createRoutineContract(@Valid ContractRequestDto contractDto);
}
