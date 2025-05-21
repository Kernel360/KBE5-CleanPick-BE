package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.dto.ContractRequestDto;

public interface ContractService {
    Contract createContract(ContractRequestDto contractDto);
}
