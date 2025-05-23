package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.cleaning.domain.exception.CleaningNotFoundException;
import com.kdev5.cleanpick.cleaning.domain.exception.CleaningOptionNotFoundException;
import com.kdev5.cleanpick.cleaning.infra.CleaningOptionRepository;
import com.kdev5.cleanpick.cleaning.infra.CleaningRepository;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.ContractOption;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.domain.exception.ContractNotFoundException;
import com.kdev5.cleanpick.contract.dto.ContractRequestDto;
import com.kdev5.cleanpick.contract.infra.*;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.customer.domain.exception.CustomerNotFoundException;
import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.exception.ManagerNotFoundException;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractDetailRepository contractDetailRepository;
    private final ContractOptionRepository contractOptionRepository;
    private final NomineeRepository nomineeRepository;
    private final RoutineContractRepository routineContractRepository;
    private final CustomerRepository customerRepository;
    private final ManagerRepository managerRepository;
    private final CleaningRepository cleaningRepository;
    private final CleaningOptionRepository cleaningOptionRepository;

    // Entity 조회
    public Customer findCustomer(Long customerId){
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    public Manager findManagerIfPresent(Long managerId){
        if (managerId == null) return null;
        return managerRepository.findById(managerId)
                .orElseThrow(()-> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND));
    }

    public RoutineContract findRoutineContractIfPresent(Long routineContractId){
        if (routineContractId == null) return null;
        return routineContractRepository.findById(routineContractId)
                .orElseThrow(()-> new ContractNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    public Cleaning findCleaning(Long cleaningId){
        return cleaningRepository.findById(cleaningId)
                .orElseThrow(()-> new CleaningNotFoundException(ErrorCode.CLEANING_NOT_FOUND));
    }

    // Contract 엔티티 저장
    public Contract saveContract(ContractRequestDto dto, Customer customer, Manager manager, Cleaning cleaning, RoutineContract routineContract) {
        return contractRepository.save(dto.toEntity(customer, manager, cleaning, routineContract));
    }

    // ContractDetail 엔티티 저장
    public ContractDetail saveContactDetail(ContractRequestDto dto, Contract newContract) {
        dto.setStatus(ContractStatus.작업전);
        return contractDetailRepository.save(dto.toEntity(newContract));
    }

    // ContractOptions 저장
    public void saveContractOptions(ContractRequestDto dto, Contract contract) {
        for (Long optionId : dto.getCleaningOptionList()) {
            CleaningOption cleaningOption = cleaningOptionRepository.findById(optionId)
                    .orElseThrow(() -> new CleaningOptionNotFoundException(ErrorCode.CLEANING_OPTION_NOT_FOUND));

            contractOptionRepository.save(dto.toOptionEntity(contract, cleaningOption));
        }
    }


    // 1회성 청소 요청글 작성
    @Transactional
    @Override
    public ContractRequestDto createOneContract(@Valid ContractRequestDto contractDto){
        Customer customer = findCustomer(contractDto.getCustomerId());
        Manager manager = findManagerIfPresent(contractDto.getManagerId());
        RoutineContract routineContract = findRoutineContractIfPresent(contractDto.getRoutineContractId());
        Cleaning cleaning = findCleaning(contractDto.getCleaningId());

        // contract - 예약 정보 저장
        Contract newContract = saveContract(contractDto, customer, manager, cleaning, routineContract);

        // contract_detail - 예약 상세 정보 저장
        ContractDetail newContractDetail = saveContactDetail(contractDto, newContract);

        // contract_option - 청소 요구사항 정보 저장
        saveContractOptions(contractDto, newContract);

        return contractDto;
    }


    // 정기 청소 요청글 작성
    @Transactional
    @Override
    public RoutineContract createRoutineContract(@Valid ContractRequestDto routinecontractDto){

        RoutineContract newRoutineContract = RoutineContract.builder()
                .discountRate(routinecontractDto.getDiscountRate())
                .routineCount(routinecontractDto.getRoutineCount())
                .contractStartDate(routinecontractDto.getContractStartDate())
                .startTime(routinecontractDto.getStartTime())
                .time(routinecontractDto.getTime())
                .day(routinecontractDto.getDay())
                .build();

        return routineContractRepository.save(newRoutineContract);
    }

}
