package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.dto.response.ContractResponseDto;
import com.kdev5.cleanpick.contract.dto.response.OneContractResponseDto;
import jakarta.validation.Valid;

import java.util.List;

public interface ContractService {
    OneContractResponseDto createOneContract(@Valid ContractRequestDto contractDto);
    List<ContractResponseDto> createRoutineContract(@Valid ContractRequestDto contractDto);
}
