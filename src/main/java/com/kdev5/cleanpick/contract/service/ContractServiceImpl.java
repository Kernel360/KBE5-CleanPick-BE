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


    @Transactional
    @Override
    public ContractRequestDto createOneContract(@Valid ContractRequestDto contractDto){
//        System.out.println("사용자 아이디 : " + (contractDto.getCustomerId() != null ? contractDto.getCustomerId() : "값 없음"));
        Customer customer = customerRepository.findById(contractDto.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
        Manager manager = null;
        if (contractDto.getManagerId() != null) {
            manager = managerRepository.findById(contractDto.getManagerId())
                    .orElseThrow(() -> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND));
        }

        RoutineContract routineContract = null;
        if ( contractDto.getRoutineContractId() != null ){
            routineContract = routineContractRepository.findById(contractDto.getRoutineContractId())
                    .orElseThrow(() -> new ContractNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));
        }

        Cleaning cleaning = cleaningRepository.findById(contractDto.getCleaningId())
                .orElseThrow(() -> new CleaningNotFoundException(ErrorCode.CLEANING_NOT_FOUND));

        // contract - 예약 정보 저장
        Contract newContract = Contract.builder()
                .customer(customer)
                .manager(manager)
                .cleaning(cleaning)
                .routineContract(routineContract)
                .contractDate(contractDto.getContractDate())
                .address(contractDto.getAddress())
                .totalPrice(contractDto.getTotalPrice())
                .totalTime(contractDto.getTotalTime())
                .personal(contractDto.isPersonal())
                .build();

        contractRepository.save(newContract);

        // contract_detail - 예약 상세 정보 저장
        contractDto.setStatus(ContractStatus.작업전); // 새로 작성한 예약글이므로 contractDeatil.status 작업전으로 설정
        ContractDetail newContractDetail = ContractDetail.builder()
                .contract(newContract)
                .housingType(contractDto.getHousingType())
                .pet(contractDto.getPet())
                .request(contractDto.getRequest())
                .status(contractDto.getStatus())
                .build();

        contractDetailRepository.save(newContractDetail);

        // contract_option - 청소 요구사항 정보 저장
        List<Long> cleaningOptionList = contractDto.getCleaningOptionList();
        for ( int i = 0; i < cleaningOptionList.size() ; i++ ){
            CleaningOption cleaningOption = cleaningOptionRepository.findById(cleaningOptionList.get(i))
                    .orElseThrow(() -> new CleaningOptionNotFoundException(ErrorCode.CLEANING_OPTION_NOT_FOUND));

            ContractOption newContractOption = ContractOption.builder()
                    .contract(newContract)
                    .cleaningOption(cleaningOption)
                    .build();

            contractOptionRepository.save(newContractOption);
        }

        return contractDto;
    }

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
