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
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ContractMatchingServiceTest {

    private NomineeRepository nomineeRepository;
    private ContractRepository contractRepository;
    private ManagerRepository managerRepository;
    private ContractMatchingService contractMatchingService;

    @BeforeEach
    void setUp() {
        nomineeRepository = mock(NomineeRepository.class);
        contractRepository = mock(ContractRepository.class);
        managerRepository = mock(ManagerRepository.class);
        contractMatchingService = new ContractMatchingService(nomineeRepository, contractRepository, managerRepository);
    }

    @Test
    @DisplayName("updateMatchingStatus - 정상 동작")
    void updateMatchingStatus_success() {
        Long managerId = 1L;
        Long contractId = 1L;
        Nominee nominee = mock(Nominee.class);

        when(nomineeRepository.findByContractAndManager(managerId, contractId))
                .thenReturn(Optional.of(nominee));

        contractMatchingService.updateMatchingStatus(managerId, contractId, MatchingStatus.REJECT);

        verify(nominee).updateStatus(MatchingStatus.REJECT);
        verify(nomineeRepository).save(nominee);
    }

    @Test
    @DisplayName("updateMatchingStatus - Nominee 없을 경우 예외 발생")
    void updateMatchingStatus_nomineeNotFound() {
        when(nomineeRepository.findByContractAndManager(any(), any()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> contractMatchingService.updateMatchingStatus(1L, 1L, MatchingStatus.REJECT))
                .isInstanceOf(NomineeException.class)
                .hasMessageContaining(ErrorCode.MATCHING_NOMINEE_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("matchManagerAndCustomer - 개인 계약일 경우 정상 처리")
    void matchManagerAndCustomer_success() {
        Long managerId = 1L;
        Long contractId = 1L;

        Nominee nominee = mock(Nominee.class);
        Manager manager = mock(Manager.class);
        Contract contract = mock(Contract.class);

        when(nomineeRepository.findByContractAndManager(managerId, contractId))
                .thenReturn(Optional.of(nominee));
        when(managerRepository.findById(managerId)).thenReturn(Optional.of(manager));
        when(contractRepository.findById(contractId)).thenReturn(Optional.of(contract));
        when(contract.isPersonal()).thenReturn(true);

        contractMatchingService.matchManagerAndCustomer(managerId, contractId);

        verify(nominee).updateStatus(MatchingStatus.ACCEPT);
        verify(contract).updateManager(manager);
        verify(contractRepository).save(contract);
    }

    @Test
    @DisplayName("matchManagerAndCustomer - 개인 계약이 아닐 경우 예외 발생")
    void matchManagerAndCustomer_notPersonalContract() {
        Nominee nominee = mock(Nominee.class);
        Manager manager = mock(Manager.class);
        Contract contract = mock(Contract.class);

        when(nomineeRepository.findByContractAndManager(any(), any()))
                .thenReturn(Optional.of(nominee));
        when(managerRepository.findById(any()))
                .thenReturn(Optional.of(manager));
        when(contractRepository.findById(any()))
                .thenReturn(Optional.of(contract));
        when(contract.isPersonal()).thenReturn(false);

        assertThatThrownBy(() -> contractMatchingService.matchManagerAndCustomer(1L, 1L))
                .isInstanceOf(ContractException.class)
                .hasMessageContaining(ErrorCode.CONTRACT_BAD_REQUEST.getMessage());
    }
}