package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.kdev5.cleanpick.contract.domain.exception.ContractException;
import com.kdev5.cleanpick.contract.domain.exception.NomineeException;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.contract.infra.NomineeRepository;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.exception.ManagerNotFoundException;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContractMatchingService {
    private final NomineeRepository nomineeRepository;
    private final ContractRepository contractRepository;
    private final ManagerRepository managerRepository;

    @Transactional
    public void acceptMatching(Long managerId, Long contractId) {
        Nominee nominee = getNominee(managerId, contractId);
        nominee.updateStatus(MatchingStatus.ACCEPT);
        nomineeRepository.save(nominee);
    }

    @Transactional
    public void rejectMatching(Long managerId, Long contractId) {
        Nominee nominee = getNominee(managerId, contractId);
        nominee.updateStatus(MatchingStatus.REJECT);
        nomineeRepository.save(nominee);
    }

    @Transactional
    public void acceptPersonalMatching(Long managerId, Long contractId) {
        acceptMatching(managerId, contractId);

        Manager manager = getManager(managerId);
        Contract contract = getContract(contractId);

        if (!contract.isPersonal()) throw new ContractException(ErrorCode.CONTRACT_BAD_REQUEST);

        contract.updateManager(manager);
        contractRepository.save(contract);

        //TODO: 알림 로직
    }

    @Transactional
    public void rejectPersonalMatching(Long managerId, Long contractId) {
        rejectMatching(managerId, contractId);
        //TODO: 알림 로직
    }

    @Transactional
    public void confirmMatching(Long customerId, Long managerId, Long contractId) {
        Contract contract = getContract(contractId);

        if (!Objects.equals(contract.getCustomer().getId(), customerId))
            throw new ContractException(ErrorCode.CONTRACT_FORBIDDEN_ACCESS);

        if (contract.getManager() != null)
            throw new ContractException(ErrorCode.CONTRACT_ALREADY_MATCHED);


        List<Nominee> allNominees = nomineeRepository.findAllByContract(contract);
        for (Nominee nominee : allNominees) {
            if (Objects.equals(nominee.getManager().getId(), managerId)) {
                nominee.updateStatus(MatchingStatus.CONFIRMED);
            } else {
                nominee.updateStatus(MatchingStatus.CLOSED);
            }
            nomineeRepository.save(nominee);
        }

        Manager manager = getManager(managerId);
        contract.updateManager(manager);
        contractRepository.save(contract);

    }

    private Nominee getNominee(Long managerId, Long contractId) {
        return nomineeRepository.findByContractAndManager(managerId, contractId).orElseThrow(() -> new NomineeException(ErrorCode.MATCHING_NOMINEE_NOT_FOUND));
    }

    private Manager getManager(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND));
    }

    private Contract getContract(Long contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractException(ErrorCode.CONTRACT_NOT_FOUND));
    }
}
