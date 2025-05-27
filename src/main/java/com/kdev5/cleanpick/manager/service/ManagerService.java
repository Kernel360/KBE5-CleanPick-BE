package com.kdev5.cleanpick.manager.service;

import com.kdev5.cleanpick.manager.infra.querydsl.ManagerRepositoryCustom;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;

    // 매니저 검색 및 필터
    public Page<ManagerResponseDto> searchManagers(
            String serviceType,
            String region,
            String keyword,
            String sortType,
            Pageable pageable
    ) {
        return managerRepository.searchManagers(serviceType, region, keyword, sortType, pageable);
    }


}
