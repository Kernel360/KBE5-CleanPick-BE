package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.cleaning.domain.QCleaning;
import com.kdev5.cleanpick.cleaning.domain.QCleaningOption;
import com.kdev5.cleanpick.contract.domain.ContractOption;
import com.kdev5.cleanpick.contract.domain.QContractOption;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ContractOptionRepositoryImpl implements ContractOptionRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ContractOption> findAllByContractId(Long contractId) {
        QContractOption contractOption = QContractOption.contractOption;
        QCleaningOption cleaningOption = QCleaningOption.cleaningOption;
        QCleaning cleaning = QCleaning.cleaning;

        return jpaQueryFactory
                .selectFrom(contractOption)
                .join(contractOption.cleaningOption, cleaningOption).fetchJoin()
                .join(cleaningOption.cleaning, cleaning).fetchJoin()
                .where(contractOption.contract.id.eq(contractId))
                .fetch();
    }
}
