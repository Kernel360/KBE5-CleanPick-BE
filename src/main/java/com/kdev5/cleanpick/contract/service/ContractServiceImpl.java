package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.cleaning.infra.CleaningRepository;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.dto.ContractRequestDto;
import com.kdev5.cleanpick.contract.infra.*;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Transactional
    @Override
    public Contract createContract(ContractRequestDto contractDto){
//        System.out.println("사용자 아이디 : " + (contractDto.getCustomerId() != null ? contractDto.getCustomerId() : "값 없음"));
        Customer customer = customerRepository.findById(contractDto.getCustomerId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 고객입니다."));
        Manager manager = null;
        if (contractDto.getManagerId() != null) {
            manager = managerRepository.findById(contractDto.getManagerId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 매니저 입니다."));
        }

        RoutineContract routineContract = null;
        if ( contractDto.getRoutineContractId() != null ){
            routineContract = routineContractRepository.findById(contractDto.getRoutineContractId())
                    .orElseThrow(() -> new RuntimeException("존재하지 않는 예약 입니다."));
        }

        Cleaning cleaning = cleaningRepository.findById(contractDto.getCleaningId())
                .orElseThrow(() -> new RuntimeException("존재하지 않는 청소 서비스 입니다."));

        return contractRepository.save(
                Contract.builder()
                        .customer(customer)
                        .manager(manager)
                        .cleaning(cleaning)
                        .routineContract(routineContract)
                        .contractDate(contractDto.getContractDate())
                        .address(contractDto.getAddress())
                        .totalPrice(contractDto.getTotalPrice())
                        .totalTime(contractDto.getTotalTime())
                        .personal(contractDto.isPersonal())
                        .build()
        );
    }

}
