package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.dto.response.ContractResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ContractService {
    ContractResponseDto createOneContract(@Valid ContractRequestDto contractDto);
    List<ContractResponseDto> createRoutineContract(@Valid ContractRequestDto contractDto);
}
