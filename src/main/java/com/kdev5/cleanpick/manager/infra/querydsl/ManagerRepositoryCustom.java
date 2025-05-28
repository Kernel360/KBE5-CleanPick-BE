package com.kdev5.cleanpick.manager.infra.querydsl;

import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ManagerRepositoryCustom {

    Page<ManagerSearchResponseDto> searchManagers(String cleaning, String region, String keyword, SortType sortType, Pageable pageable);

}
