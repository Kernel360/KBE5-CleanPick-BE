package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.kdev5.cleanpick.contract.domain.exception.ContractException;
import com.kdev5.cleanpick.contract.domain.exception.ContractNotFoundException;
import com.kdev5.cleanpick.contract.domain.exception.NomineeException;
import com.kdev5.cleanpick.contract.infra.ContractRepository;
import com.kdev5.cleanpick.contract.infra.NomineeBulkRepository;
import com.kdev5.cleanpick.contract.infra.NomineeRepository;
import com.kdev5.cleanpick.contract.service.support.IntervalTree;
import com.kdev5.cleanpick.contract.service.support.TimeInterval;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.exception.ManagerNotFoundException;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContractMatchingService {
    private final NomineeRepository nomineeRepository;
    private final NomineeBulkRepository nomineeBulkRepository;
    private final ContractRepository contractRepository;
    private final ManagerRepository managerRepository;
    private final IntervalTree intervalTree;

    private static final int DEFAULT_SEARCH_RADIUS_METERS = 20000;


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

        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ManagerNotFoundException(ErrorCode.MANAGER_NOT_FOUND));
        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractException(ErrorCode.CONTRACT_NOT_FOUND));

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

    private Nominee getNominee(Long managerId, Long contractId) {
        return nomineeRepository.findByContractAndManager(managerId, contractId).orElseThrow(() -> new NomineeException(ErrorCode.MATCHING_NOMINEE_NOT_FOUND));
    }

    //매칭 알고리즘
    @Transactional
    public void requestCleaning(Long contractId, double lat, double lon, LocalDateTime start, LocalDateTime end) {

        //--------------------------1차 필터링
        List<Manager> distanceFiltered = filterManagersByDistanceAndSchedule(lat, lon, start, end);

        //--- ----------------------2차 필터링
        Contract contract = contractRepository.findById(contractId).orElseThrow(() -> new ContractNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));
        List<Manager> finalFiltered = distanceFiltered.stream()
                .filter(m -> m.isAvailableIn(start, end))
                .filter(m -> m.supports(contract.getCleaning()))
                .toList();

        List<Nominee> nominees = finalFiltered.stream().map(m -> Nominee.builder().contract(contract).manager(m).build()).collect(Collectors.toList());
        nomineeBulkRepository.saveAll(nominees);

        //TODO: 알림 로직

    }

    @Transactional
    public void requestRoutineCleaning(Long contractId, double lat, double lon, List<LocalDateTime> contractDates) {

        List<Manager> candidateManagers = findAvailableManagersByDistance(lat, lon);

        Contract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new ContractNotFoundException(ErrorCode.CONTRACT_NOT_FOUND));

        List<TimeInterval> intervals = contractDates.stream()
                .map(startDateTime -> new TimeInterval(startDateTime, startDateTime.plusHours(contract.getTotalTime())))
                .toList();

        List<Manager> finalFiltered = candidateManagers.stream()
                .filter(manager -> intervals.stream().allMatch(interval ->
                        intervalTree.isAvailable(manager.getId(), interval)
                                && manager.isAvailableIn(interval.getStart(), interval.getEnd())))
                .filter(manager -> manager.supports(contract.getCleaning())).toList();


        List<Nominee> nominees = finalFiltered.stream()
                .map(manager -> Nominee.builder().contract(contract).manager(manager).build())
                .toList();

        nomineeBulkRepository.saveAll(nominees);

        // TODO: 알림 로직 추가
    }


    public List<Manager> filterManagersByDistanceAndSchedule(double lat, double lon, LocalDateTime start, LocalDateTime end) {
        List<Manager> managerIdsByDistance = findAvailableManagersByDistance(lat, lon); // 거리 기반 필터링
        return filterManagersByContractTime(managerIdsByDistance, start, end); // 계약(일정)기반 필터링
    }


    private List<Manager> findAvailableManagersByDistance(double lat, double lon) {
        return managerRepository.findManagersWithinRadius(lat, lon, DEFAULT_SEARCH_RADIUS_METERS);
    }

    private List<Manager> filterManagersByContractTime(List<Manager> managerIds, LocalDateTime start, LocalDateTime end) {
        TimeInterval requested = new TimeInterval(start, end);
        return managerIds.stream()
                .filter(m -> intervalTree.isAvailable(m.getId(), requested))
                .collect(Collectors.toList());
    }

}
