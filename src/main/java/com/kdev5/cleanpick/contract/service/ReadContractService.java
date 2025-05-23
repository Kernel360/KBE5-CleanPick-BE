package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.exception.ContractNotFoundException;
import com.kdev5.cleanpick.contract.infra.ContractDetailRepository;
import com.kdev5.cleanpick.contract.infra.ContractOptionRepository;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractDetailResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractOptionResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReadContractService {
    private final ContractDetailRepository contractDetailRepository;
    private final ContractOptionRepository contractOptionRepository;

    public ReadContractDetailResponseDto readContractDetail(Long contractId) {
        ContractDetail contractDetail = contractDetailRepository.findByContractId(contractId).orElseThrow(ContractNotFoundException::new);
        List<ReadContractOptionResponseDto> options = contractOptionRepository.findAllByContractId(contractId).stream()
                .map(option ->
                        ReadContractOptionResponseDto.builder()
                                .name(option.getCleaningOption().getName())
                                .type(option.getCleaningOption().getType()).
                                build()
                ).toList();

        return ReadContractDetailResponseDto.fromEntity(contractDetail, options);
    }
}
