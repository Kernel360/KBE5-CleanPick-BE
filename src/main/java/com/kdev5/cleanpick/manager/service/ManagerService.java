package com.kdev5.cleanpick.manager.service;

import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;

    // 필터, 검색, 정렬을 적용한 매니저 리스트 반환
    public Page<ManagerSearchResponseDto> searchManagers(
            String serviceType,
            String region,
            String keyword,
            SortType sortType,
            Pageable pageable
    ) {
        return managerRepository.searchManagers(serviceType, region, keyword, sortType, pageable);
    }


}
