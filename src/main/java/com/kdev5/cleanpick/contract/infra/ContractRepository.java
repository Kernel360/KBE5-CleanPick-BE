package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.infra.querydsl.ContractRepositoryCustom;
import com.kdev5.cleanpick.manager.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    @Query("select c from Contract c join fetch c.manager where c.manager = :manager and c.status = :status and c.contractDate between :start and :end")
    List<Contract> findContractsWithManagerByManagerAndStatusAndContractDateBetween(
            @Param("manager") Manager manager,
            @Param("status") ContractStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);

    @Query("select sum(c.totalPrice) from Contract c where c.manager = :manager and c.contractDate between :start and :end")
    Long sumMonthlyTotalPriceByManager(
            @Param("manager") Manager manager,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
