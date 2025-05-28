package com.kdev5.cleanpick.contract.service;


import com.kdev5.cleanpick.cleaning.domain.Cleaning;
import com.kdev5.cleanpick.cleaning.domain.CleaningOption;
import com.kdev5.cleanpick.cleaning.domain.exception.CleaningNotFoundException;
import com.kdev5.cleanpick.cleaning.domain.exception.CleaningOptionNotFoundException;
import com.kdev5.cleanpick.cleaning.infra.CleaningOptionRepository;
import com.kdev5.cleanpick.cleaning.infra.CleaningRepository;
import com.kdev5.cleanpick.common.util.ContractDateUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.RoutineContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.service.dto.request.ContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.request.UpdateContractRequestDto;
import com.kdev5.cleanpick.contract.service.dto.response.OneContractResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.RoutineContractResponseDto;
import com.kdev5.cleanpick.contract.domain.exception.ContractException;
import com.kdev5.cleanpick.contract.infra.*;
import com.kdev5.cleanpick.customer.domain.Customer;
import com.kdev5.cleanpick.customer.domain.exception.CustomerNotFoundException;
import com.kdev5.cleanpick.customer.infra.repository.CustomerRepository;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.ManagerAvailableTime;
import com.kdev5.cleanpick.manager.domain.exception.ManagerNotAvailableTimeException;
import com.kdev5.cleanpick.manager.domain.exception.ManagerNotFoundException;
import com.kdev5.cleanpick.manager.infra.repository.ManagerAvailableTimeRepository;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
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
    private final ManagerAvailableTimeRepository managerAvailableTimeRepository;

    // Entity 조회
    public Customer findCustomer(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(ErrorCode.CUSTOMER_NOT_FOUND));
    }

    public Manager findManagerIfPresent(Long managerId) {
        System.out.println("findManagerIfPresent: " + managerId);
        if (managerId == null) return null;
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND));
    }

    public RoutineContract findRoutineContractIfPresent(Long routineContractId) {
        if (routineContractId == null) return null;
        return routineContractRepository.findById(routineContractId)
                .orElseThrow(() -> new ContractException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    public Cleaning findCleaning(Long cleaningId) {
        return cleaningRepository.findById(cleaningId)
                .orElseThrow(() -> new CleaningNotFoundException(ErrorCode.CLEANING_NOT_FOUND));
    }

    public Contract findContract(Long contractId) {
        return contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractException(ErrorCode.CONTRACT_NOT_FOUND));
    }

    public ContractDetail findContractDetail(Long contractId) {
        return contractDetailRepository.findById(contractId)
                .orElseThrow(() -> new ContractException(ErrorCode.CONTRACT_DETAIL_NOT_FOUND));
    }

    // Contract 저장
    public Contract saveContract(ContractRequestDto dto, Customer customer, Manager manager, Cleaning cleaning, RoutineContract routineContract) {
        dto.setStatus(ContractStatus.작업전);
        return contractRepository.save(dto.toEntity(customer, manager, cleaning, routineContract));
    }

    // ContractDetail 저장
    public ContractDetail saveContactDetail(ContractRequestDto dto, Contract newContract) {
        return contractDetailRepository.save(dto.toEntity(newContract));
    }

    // ContractOptions 저장
    public List<Long> saveContractOptions(ContractRequestDto dto, Contract contract) {
        List<Long> cleaningOptions = new ArrayList<>();
        for (Long optionId : dto.getCleaningOptionList()) {
            CleaningOption cleaningOption = cleaningOptionRepository.findById(optionId)
                    .orElseThrow(() -> new CleaningOptionNotFoundException(ErrorCode.CLEANING_OPTION_NOT_FOUND));

            cleaningOptions.add(cleaningOption.getId());
            contractOptionRepository.save(dto.toOptionEntity(contract, cleaningOption));
        }

        return cleaningOptions;
    }

    // RoutineContract 저장
    public RoutineContract saveRoutineContract(ContractRequestDto dto) {
        return routineContractRepository.save(dto.toEntity());
    }


    // 1회성 청소 요청글 작성
    @Transactional
    @Override
    public OneContractResponseDto createOneContract(@Valid ContractRequestDto contractDto){
        Customer customer = findCustomer(contractDto.getCustomerId());
        Manager manager = findManagerIfPresent(contractDto.getManagerId());
        RoutineContract routineContract = findRoutineContractIfPresent(contractDto.getRoutineContractId());
        Cleaning cleaning = findCleaning(contractDto.getCleaningId());

        // contract - 예약 정보 저장
        Contract newContract = saveContract(contractDto, customer, manager, cleaning, routineContract);

        // contract_detail - 예약 상세 정보 저장
        ContractDetail newContractDetail = saveContactDetail(contractDto, newContract);

        // contract_option - 청소 요구사항 정보 저장
        List<Long> cleaningOptions = saveContractOptions(contractDto, newContract);

        return OneContractResponseDto.fromEntity(newContract, newContractDetail, cleaningOptions, null);
    }


    // 정기 청소 요청글 작성
    @Transactional
    @Override
    public RoutineContractResponseDto createRoutineContract(@Valid ContractRequestDto routinecontractDto){

        List<OneContractResponseDto> contractResponseDtoList = new ArrayList<>();

        // 정기 예약 정보 저장
        RoutineContract newRoutineContract = saveRoutineContract(routinecontractDto);

        // 2, 요일 파싱
        List<DayOfWeek> dayList = routinecontractDto.getDayOfWeek();

        // 3. 반복 예약 날짜 계산
        List<LocalDateTime> contractDates = ContractDateUtil.generateContractDates(routinecontractDto.getStartTime(), dayList, routinecontractDto.getRoutineCount());

        for (LocalDateTime contractDate : contractDates) {
            System.out.println(contractDate);
        }

        // 4. Entity 조회
        Customer customer = findCustomer(routinecontractDto.getCustomerId());
        Manager manager = findManagerIfPresent(routinecontractDto.getManagerId());
        Cleaning cleaning = findCleaning(routinecontractDto.getCleaningId());

        // 5. 각 날짜마다 Contract, ContractDetail, ContractOption 저장
        for (LocalDateTime date : contractDates) {
            routinecontractDto.setStatus(ContractStatus.작업전);
            routinecontractDto.setContractDate(date);

            // contract - 예약 정보 저장
            Contract newContract = saveContract(routinecontractDto, customer, manager, cleaning, newRoutineContract);

            // contract_detail - 예약 상세 정보 저장
            ContractDetail newContractDetail = saveContactDetail(routinecontractDto, newContract);

            // contract_option - 청소 요구사항 정보 저장
            List<Long> cleaningOptions = saveContractOptions(routinecontractDto, newContract);

            contractResponseDtoList.add(OneContractResponseDto.fromEntity(newContract, newContractDetail, cleaningOptions, newRoutineContract));
        }

        return RoutineContractResponseDto.fromEntity(newRoutineContract, contractResponseDtoList);
    }

    private void validateManagerAvailableTime(Manager manager, LocalTime startTime, DayOfWeek dayOfWeek, int durationHours) {
        List<ManagerAvailableTime> availableTimes = managerAvailableTimeRepository.findByManagerAndDayOfWeek(manager, dayOfWeek);

        LocalTime endTime = startTime.plusHours(durationHours);

        boolean isWithinAvailableTime = availableTimes.stream().anyMatch(time ->
                !startTime.isBefore(time.getStartTime()) && !endTime.isAfter(time.getEndTime()));

        if (!isWithinAvailableTime) {
            throw new ManagerNotAvailableTimeException(ErrorCode.MANAGER_NOT_AVAILABLE_TIME);
        }
    }

    private void validateNoScheduleConflict(Manager manager, Long currentContractId, LocalDateTime start, int durationHours) {
        LocalDateTime end = start.plusHours(durationHours);

        List<Contract> existingContracts = contractRepository.findByManager(manager);

        for (Contract other : existingContracts) {
            if (other.getId().equals(currentContractId)) continue; // 현재 수정 중인 계약은 제외

            LocalDateTime otherStart = other.getContractDate();
            LocalDateTime otherEnd = otherStart.plusHours(other.getTotalTime());

            boolean overlap = !(end.isBefore(otherStart) || start.isAfter(otherEnd));
            if (overlap) {
                throw new ManagerNotAvailableTimeException(ErrorCode.MANAGER_NOT_AVAILABLE_TIME);
            }
        }
    }

    @Transactional
    @Override
    public void updateOneContract(@Valid UpdateContractRequestDto contractDto, Long contractId) {
        //request, contractDate, pet 수정
        Contract contract = findContract(contractId);
        ContractDetail contractDetail = findContractDetail(contractId);

        // 매니저 매칭 안된 계약이면 바로 수정, 매칭된 계약인 경우 일정 확인 후 수정 (일정 겹친다 알림떠서 매칭 취소할지말지 하는 팝업이 떠야할 수도 있을거 같음)
        if ( contract.getManager() == null ) contract.updateDate(contractDto.getContractDate());
        else {
            Manager manager = findManagerIfPresent(contract.getManager().getId());
            // 매니저의 기존 일정과 안 겹치는지 + 매니저 가능 시간인지 확인 필요
            LocalDateTime newDateTime = contractDto.getContractDate();
            int durationHours = contract.getTotalTime();

            // 1. 매니저 가능한 시간인지 확인
            validateManagerAvailableTime(manager, newDateTime.toLocalTime(), newDateTime.getDayOfWeek(), durationHours);

            // 2. 매니저 다른 계약과 겹치지 않는지 확인
            validateNoScheduleConflict(manager, contract.getId(), newDateTime, durationHours);

            contract.updateDate(newDateTime);
        }

        contractDetail.updateInfo(contractDto.getRequest(), contractDto.getPet());

    }

    @Transactional
    @Override
    public void deleteOneContract(@Valid Long contractId) {
        Contract contract = findContract(contractId);
        ContractDetail contractDetail = findContractDetail(contractId);
        contract.softDelete();
        contractDetail.softDelete();
    }

}
