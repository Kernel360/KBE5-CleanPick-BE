package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.manager.domain.Manager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface ContractRepositoryCustom {
    Page<Contract> findByFilter(Long userId, String role, ContractFilterStatus filter, Pageable pageable);

    List<Contract> findContractsByManagerAndStatusWithinDateRange(Manager manager, List<ContractStatus> statuses, LocalDateTime start, LocalDateTime end);

    Integer sumMonthlyTotalPriceByManager(Manager manager, List<ContractStatus> statuses, LocalDateTime start, LocalDateTime end);
}
