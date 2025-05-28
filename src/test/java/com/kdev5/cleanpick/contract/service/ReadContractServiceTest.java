package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.common.util.DateTimeUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.ContractOption;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.domain.exception.ContractException;
import com.kdev5.cleanpick.contract.infra.ContractDetailRepository;
import com.kdev5.cleanpick.contract.infra.ContractOptionRepository;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractDetailResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadContractResponseDto;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReadContractServiceTest {

    private ContractRepository contractRepository;
    private ContractDetailRepository contractDetailRepository;
    private ContractOptionRepository contractOptionRepository;

    private ReadContractService readContractService;

    Long contractId = 1L;
    Customer customer = mock(Customer.class);
    Manager manager = mock(Manager.class);
    Cleaning cleaning = Cleaning.builder().serviceName("특수청소").build();
    Contract contract = Contract.builder().manager(manager).customer(customer).cleaning(cleaning).status(ContractStatus.작업전).contractDate(LocalDateTime.now()).build();

    @BeforeEach
    void setUp() {
        contractRepository = mock(ContractRepository.class);
        contractDetailRepository = mock(ContractDetailRepository.class);
        contractOptionRepository = mock(ContractOptionRepository.class);
        readContractService = new ReadContractService(contractRepository, contractDetailRepository, contractOptionRepository);


    }

    @Test
    void 계약상세조회_성공() {
        // given
        ContractDetail contractDetail = ContractDetail.builder()
                .contract(contract)
                .pet("강아지")
                .request("강아지 만지지 말아주세요")
                .housingType("아파트")
                .build();

        CleaningOption cleaningOption = CleaningOption.builder()
                .name("보통")
                .type("ROOM_SIZE")
                .build();

        ContractOption contractOption = ContractOption.builder()
                .cleaningOption(cleaningOption)
                .contract(contract)
                .build();

        when(contractDetailRepository.findByContractId(contractId)).thenReturn(Optional.of(contractDetail));
        when(contractOptionRepository.findAllByContractId(contractId)).thenReturn(List.of(contractOption));

        DateTimeUtil.DateTimeParts parts = DateTimeUtil.splitDateTime(contractDetail.getContract().getContractDate());

        // when
        ReadContractDetailResponseDto result = readContractService.readContractDetail(contractId);

        // then
        assertThat(result.getServiceName()).isEqualTo("특수청소");
        assertThat(result.getContractDate()).isEqualTo(parts.getDate());
        assertThat(result.getContractStartTime()).isEqualTo(parts.getTime());

        assertThat(result.getOptions()).hasSize(1);
        assertThat(result.getOptions().get(0).name()).isEqualTo("보통");
        assertThat(result.getOptions().get(0).type()).isEqualTo("ROOM_SIZE");
    }

    @Test
    void 계약상세조회_실패_존재하지않는계약() {
        // given
        Long contractId = 999L;
        when(contractDetailRepository.findByContractId(contractId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> readContractService.readContractDetail(contractId))
                .isInstanceOf(ContractException.class)
                .hasMessageContaining(ErrorCode.CONTRACT_NOT_FOUND.getMessage());
    }

    @Test
    void 계약리스트조회_성공() {
        // given

        when(contractRepository.findByFilter(eq(1L), eq(ContractFilterStatus.WAITING_MATCH), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(contract)));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<ReadContractResponseDto> result = readContractService.readContracts(ContractFilterStatus.WAITING_MATCH, pageable);

        // then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).contractStatus()).isEqualTo(ContractStatus.작업전.toString());

    }
}
