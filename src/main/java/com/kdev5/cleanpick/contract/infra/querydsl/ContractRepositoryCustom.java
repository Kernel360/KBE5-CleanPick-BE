package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ContractRepositoryCustom {
    Page<Contract> findByFilter(Long userId, String role, ContractFilterStatus filter, Pageable pageable);
}
