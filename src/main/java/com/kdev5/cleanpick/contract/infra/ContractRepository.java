package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.infra.querydsl.ContractRepositoryCustom;
import com.kdev5.cleanpick.manager.domain.Manager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
    List<Contract> findByManager(Manager manager);
}
