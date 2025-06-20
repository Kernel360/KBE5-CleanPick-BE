package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.infra.NomineeRepository;
import com.kdev5.cleanpick.contract.service.dto.response.ReadAcceptedMatchingResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadConfirmedMatchingResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadRequestedMatchingResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ReadNomineeService {
    private final NomineeRepository nomineeRepository;

    @Transactional(readOnly = true)
    public Page<ReadRequestedMatchingResponseDto> readRequestedMatching(Long managerId, Boolean isPersonal, Pageable pageable) {

        Page<Nominee> nominees = nomineeRepository.findRequestedMatching(managerId, isPersonal, pageable);
        return nominees.map(nominee ->
                ReadRequestedMatchingResponseDto.fromEntity(nominee.getContract(), nominee.getId())
        );

    }

    @Transactional(readOnly = true)
    public Page<ReadAcceptedMatchingResponseDto> readAcceptedMatching(Long managerId, ServiceName type, Pageable pageable) {

        Page<Nominee> nominees = nomineeRepository.readAcceptedMatching(managerId, type, pageable);
        return nominees.map(nominee ->
                ReadAcceptedMatchingResponseDto.fromEntity(nominee.getContract(), nominee.getId())
        );
    }

    @Transactional(readOnly = true)
    public Page<ReadConfirmedMatchingResponseDto> readConfirmedMatching(Long managerId, ServiceName serviceType, String sortType, Pageable pageable) {

        Page<Nominee> nominees = nomineeRepository.findConfirmedMatching(managerId, serviceType, sortType.toLowerCase(), pageable);
        return nominees.map(nominee ->
                ReadConfirmedMatchingResponseDto.fromEntity(nominee.getContract())
        );
    }
}
