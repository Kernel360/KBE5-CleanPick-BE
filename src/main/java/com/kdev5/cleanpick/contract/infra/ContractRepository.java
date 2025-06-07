package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.infra.querydsl.ContractRepositoryCustom;
import com.kdev5.cleanpick.manager.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    List<Contract> findByManager(Manager manager);

    @Query("SELECT c FROM Contract c WHERE c.manager.id IS NOT NULL AND c.contractDate >= :startOfDay")
    List<Contract> findContractsFrom(@Param("startOfDay") LocalDateTime startOfDay);

}
