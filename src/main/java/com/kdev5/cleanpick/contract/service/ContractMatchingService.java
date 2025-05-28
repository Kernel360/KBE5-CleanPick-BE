package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.kdev5.cleanpick.contract.domain.exception.NomineeException;
import com.kdev5.cleanpick.contract.infra.NomineeRepository;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContractMatchingService {
    private final NomineeRepository nomineeRepository;

    @Transactional
    public void rejectMatchingRequest(Long managerId, Long contractId) {
        Nominee nominee = getNominee(managerId, contractId);
        nominee.updateStatus(MatchingStatus.REJECT);
        nomineeRepository.save(nominee);
    }

    @Transactional
    public void acceptMatchingRequest(Long managerId, Long contractId) {
        Nominee nominee = getNominee(managerId, contractId);
        nominee.updateStatus(MatchingStatus.ACCEPT);
        nomineeRepository.save(nominee);
    }

    private Nominee getNominee(Long managerId, Long contractId) {
        return nomineeRepository.findByContractAndManager(managerId, contractId).orElseThrow(() -> new NomineeException(ErrorCode.MATCHING_NOMINEE_NOT_FOUND));
    }
}
