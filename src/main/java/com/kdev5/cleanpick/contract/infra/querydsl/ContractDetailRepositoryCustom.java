package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.contract.domain.ContractDetail;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ContractDetailRepositoryCustom {
    Optional<ContractDetail> findByContractId(@Param("contractId") Long contractId);
}
