package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.infra.querydsl.ContractRepositoryCustom;
import com.kdev5.cleanpick.manager.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {

    @Query("select sum(c.totalPrice) from Contract c where c.manager = :manager and c.contractDate between :start and :end")
    Long sumMonthlyTotalPriceByManager(
            @Param("manager") Manager manager,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end);
}
