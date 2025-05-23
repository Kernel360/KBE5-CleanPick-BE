package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.infra.querydsl.ContractRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContractRepository extends JpaRepository<Contract, Long>, ContractRepositoryCustom {
}