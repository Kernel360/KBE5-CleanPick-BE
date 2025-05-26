package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.dto.response.ContractResponseDto;
import com.kdev5.cleanpick.contract.service.ContractService;
import com.kdev5.cleanpick.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // 1. 청소 요청 글 작성
    // 1-1. 1회성 청소
    @PostMapping("/one")
    public ApiResponse<ContractResponseDto> createContract(@RequestBody @Valid ContractRequestDto contractDto) {
        ContractResponseDto newContract = contractService.createOneContract(contractDto);
        return ApiResponse.ok(newContract);
    }

    // 1-2. 정기 청소
    @PostMapping("/routine")
    public ApiResponse<List<ContractResponseDto>> createRoutineContract(@RequestBody @Valid ContractRequestDto contractDto) {
        List<ContractResponseDto> newContracts = contractService.createRoutineContract(contractDto);
//        return ApiResponse.ok(newContract);
        return ApiResponse.ok(newContracts);
    }


}
