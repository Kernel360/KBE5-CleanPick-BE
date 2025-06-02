package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.exception.ContractException;
import com.kdev5.cleanpick.contract.infra.ContractDetailRepository;
import com.kdev5.cleanpick.contract.infra.ContractOptionRepository;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.contract.service.dto.response.ReadConfirmedMatchingResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractDetailResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractOptionResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractResponseDto;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadContractService {
    private final ContractRepository contractRepository;
    private final ContractDetailRepository contractDetailRepository;
    private final ContractOptionRepository contractOptionRepository;


    public ReadContractDetailResponseDto readContractDetail(Long contractId) {
        ContractDetail contractDetail = contractDetailRepository.findByContractId(contractId).orElseThrow(() -> new ContractException(ErrorCode.CONTRACT_NOT_FOUND));
        List<ReadContractOptionResponseDto> options = contractOptionRepository.findAllByContractId(contractId).stream()
                .map(option ->
                        ReadContractOptionResponseDto.builder()
                                .name(option.getCleaningOption().getName())
                                .type(option.getCleaningOption().getType()).
                                build()
                ).toList();

        return ReadContractDetailResponseDto.fromEntity(contractDetail, options);
    }

    public Page<ReadContractResponseDto> readCustomerContracts(Long userId, String role, ContractFilterStatus status, Pageable pageable) {
        Page<Contract> contracts = contractRepository.findByFilter(userId, role, status, pageable);
        return contracts.map(ReadContractResponseDto::fromEntity);

    }

    public Page<ReadConfirmedMatchingResponseDto> readManagerCompletedContracts(Long userId, String role, Pageable pageable) {
        Page<Contract> contracts = contractRepository.findByFilter(userId, role, ContractFilterStatus.COMPLETED, pageable);
        return contracts.map(ReadConfirmedMatchingResponseDto::fromEntity);
    }
}
