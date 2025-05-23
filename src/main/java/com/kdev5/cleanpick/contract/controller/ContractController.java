package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.dto.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.ContractService;
import com.kdev5.cleanpick.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    // 1. 청소 요청 글 작성
    // 1-1. 단기 청소
    @PostMapping("/one")
    public ApiResponse<ContractRequestDto> createContract(@RequestBody @Valid ContractRequestDto contractDto) {
        ContractRequestDto newContract = contractService.createOneContract(contractDto);
        return ApiResponse.ok(newContract);
    }

    // 1-2. 정기 청소
    @PostMapping("/routine")
    public ApiResponse<List<ContractRequestDto>> createRoutineContract(@RequestBody @Valid ContractRequestDto contractRequestDto) {
        RoutineContract newRoutineContract = contractService.createRoutineContract(contractRequestDto);
        List<ContractRequestDto> newContractList = new ArrayList<>();

        // startTime을 기준으로 각 예약 DTO에 ContractDate 부여
        LocalDateTime currDate = contractRequestDto.getStartTime();
        for ( int i = 0 ; i < contractRequestDto.getRoutineCount() ; i++ ){
//            contractRequestDto.setContractDate(currDate);
            ContractRequestDto singleContract = contractService.createOneContract(contractRequestDto);
            newContractList.add(singleContract);
            currDate = currDate.plusDays(1);
        }

        return ApiResponse.ok(newContractList);
    }


}
