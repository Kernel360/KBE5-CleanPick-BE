package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.kdev5.cleanpick.contract.domain.exception.ContractException;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.contract.infra.NomineeRepository;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

class ContractMatchingServiceTest {

    @Mock
    private NomineeRepository nomineeRepository;

    @Mock
    private ContractRepository contractRepository;

    @Mock
    private ManagerRepository managerRepository;

    @InjectMocks
    private ContractMatchingService contractMatchingService;

    private final Long managerId = 1L;
    private final Long contractId = 100L;

    private Nominee nominee;
    private Manager manager;
    private Contract contract;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        nominee = mock(Nominee.class);
        manager = mock(Manager.class);
        contract = mock(Contract.class);
    }

    @Test
    void acceptPersonalMatching_정상_호출() {
        // given
        given(nomineeRepository.findByContractAndManager(managerId, contractId)).willReturn(Optional.of(nominee));
        given(managerRepository.findById(managerId)).willReturn(Optional.of(manager));
        given(contractRepository.findById(contractId)).willReturn(Optional.of(contract));
        given(contract.isPersonal()).willReturn(true);

        // when
        contractMatchingService.acceptPersonalMatching(managerId, contractId);

        // then
        then(nominee).should().updateStatus(MatchingStatus.ACCEPT);
        then(nomineeRepository).should().save(nominee);
        then(contract).should().updateManager(manager);
        then(contractRepository).should().save(contract);
    }

    @Test
    void acceptPersonalMatching_개인아니면_예외() {
        // given
        given(nomineeRepository.findByContractAndManager(managerId, contractId))
                .willReturn(Optional.of(nominee));
        given(managerRepository.findById(managerId)).willReturn(Optional.of(manager));
        given(contractRepository.findById(contractId)).willReturn(Optional.of(contract));
        given(contract.isPersonal()).willReturn(false);

        // when & then
        assertThatThrownBy(() -> contractMatchingService.acceptPersonalMatching(managerId, contractId))
                .isInstanceOf(ContractException.class)
                .hasMessageContaining(ErrorCode.CONTRACT_BAD_REQUEST.getMessage());
    }

    @Test
    void rejectPersonalMatching_정상_호출() {
        // given
        given(nomineeRepository.findByContractAndManager(managerId, contractId))
                .willReturn(Optional.of(nominee));

        // when
        contractMatchingService.rejectPersonalMatching(managerId, contractId);

        // then
        then(nominee).should().updateStatus(MatchingStatus.REJECT);
        then(nomineeRepository).should().save(nominee);
    }
}