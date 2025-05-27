package com.kdev5.cleanpick.contract.infra;

import com.kdev5.cleanpick.contract.domain.ContractOption;
import com.kdev5.cleanpick.contract.infra.querydsl.ContractOptionRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContractOptionRepository extends JpaRepository<ContractOption, Long> {
}
