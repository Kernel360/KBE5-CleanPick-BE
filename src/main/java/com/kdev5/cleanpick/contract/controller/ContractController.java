package com.kdev5.cleanpick.contract.controller;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.dto.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.ContractService;
import com.kdev5.cleanpick.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contract")
@RequiredArgsConstructor
public class ContractController {

    private final ContractService contractService;

    @PostMapping("/test")
    public ApiResponse<Contract> test(@RequestBody @Valid Contract contract) {
        return ApiResponse.ok(contract);
    }

    @PostMapping("/createContract")
    public ApiResponse<Contract> createContract(@RequestBody @Valid ContractRequestDto contractDto) {
        Contract newContract = contractService.createContract(contractDto);
        return ApiResponse.ok(newContract);
    }

}
