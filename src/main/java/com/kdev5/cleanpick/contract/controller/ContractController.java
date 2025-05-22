package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.dto.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.ContractService;
import com.kdev5.cleanpick.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/test")
    public ApiResponse<Contract> test(@RequestBody @Valid Contract contract) {
        return ApiResponse.ok(contract);
    }

    // 1. 청소 요청 글 작성
    // 1-1. 단기 청소
    @PostMapping("/createOneContract")
    public ApiResponse<ContractRequestDto> createContract(@RequestBody @Valid ContractRequestDto contractDto) {
        ContractRequestDto newContract = contractService.createOneContract(contractDto);
        return ApiResponse.ok(newContract);
    }

    // 1-2. 정기 청소
    @PostMapping("/createRoutineContract")
    public ApiResponse<List<Contract>> createRoutineContract(@RequestBody @Valid ContractRequestDto contractRequestDto) {
        RoutineContract newRoutinecontract = contractService.createRoutineContract(contractRequestDto);
        List<Contract> newContractList = new ArrayList<>();

        for ( int i = 0 ; i < contractRequestDto.getRoutineCount() ; i++ ){
//            newContractList.add(contractService.createOneContract(null));
        }

        return ApiResponse.ok(newContractList);
    }


}
