package com.kdev5.cleanpick.contract.service;

import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.infra.NomineeRepository;
import com.kdev5.cleanpick.contract.service.dto.response.ReadAcceptedMatchingResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadConfirmedMatchingResponseDto;
import com.kdev5.cleanpick.contract.service.dto.response.ReadRequestedMatchingResponseDto;
import com.kdev5.cleanpick.manager.service.ManagerService;
import com.kdev5.cleanpick.manager.service.dto.response.ReadManagerResponseDto;
import com.kdev5.cleanpick.review.Infra.ReviewRepository;
import com.querydsl.core.types.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ReadNomineeService {
    private final NomineeRepository nomineeRepository;
    private final ReviewRepository reviewRepository;

    private final ManagerService managerService;

    @Transactional(readOnly = true)
    public Page<ReadRequestedMatchingResponseDto> readRequestedMatching(Long managerId, Boolean isPersonal, Pageable pageable) {

        Page<Nominee> nominees = nomineeRepository.findRequestedMatching(managerId, isPersonal, pageable);
        return nominees.map(nominee ->
                ReadRequestedMatchingResponseDto.fromEntity(nominee.getContract(), nominee.getId())
        );

    }

    @Transactional(readOnly = true)
    public Page<ReadAcceptedMatchingResponseDto> readAcceptedMatching(Long managerId, ServiceName type, Pageable pageable) {

        Page<Nominee> nominees = nomineeRepository.readAcceptedMatching(managerId, type, pageable);
        return nominees.map(nominee ->
                ReadAcceptedMatchingResponseDto.fromEntity(nominee.getContract(), nominee.getId())
        );
    }

    @Transactional(readOnly = true)
    public Page<ReadConfirmedMatchingResponseDto> readConfirmedMatching(Long managerId, ServiceName serviceType, String sortType, Pageable pageable) {

        Page<Nominee> nominees = nomineeRepository.findConfirmedMatching(managerId, serviceType, sortType.toLowerCase(), pageable);
        return nominees.map(nominee ->
                ReadConfirmedMatchingResponseDto.fromEntity(nominee.getContract())
        );
    }

    @Transactional(readOnly = true)
    public Page<ReadManagerResponseDto> readAcceptedNominee(Long contractId, Order order, Pageable pageable) {

        Page<Long> managerIdsPage = nomineeRepository.findAcceptManagerIds(contractId, order, pageable);

        List<Long> sortedManagerIds = managerIdsPage.getContent();

        List<ReadManagerResponseDto> unorderedDtos = managerService.readManagerInfos(sortedManagerIds);

        Map<Long, ReadManagerResponseDto> dtoMap = unorderedDtos.stream()
                .collect(Collectors.toMap(ReadManagerResponseDto::getManagerId, dto -> dto));

        List<ReadManagerResponseDto> orderedDtos = sortedManagerIds.stream()
                .map(dtoMap::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new PageImpl<>(orderedDtos, pageable, managerIdsPage.getTotalElements());
    }
}
