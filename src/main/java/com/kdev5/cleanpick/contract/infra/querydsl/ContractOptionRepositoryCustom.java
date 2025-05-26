package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.contract.domain.ContractOption;

import java.util.List;

public interface ContractOptionRepositoryCustom {
    List<ContractOption> findAllByContractId(Long contractId);
}
