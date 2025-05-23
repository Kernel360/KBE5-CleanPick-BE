package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.cleaning.domain.QCleaning;
import com.kdev5.cleanpick.contract.domain.ContractDetail;
import com.kdev5.cleanpick.contract.domain.QContract;
import com.kdev5.cleanpick.contract.domain.QContractDetail;
import com.kdev5.cleanpick.customer.domain.QCustomer;
import com.kdev5.cleanpick.manager.domain.QManager;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

@RequiredArgsConstructor
public class ContractDetailRepositoryImpl implements ContractDetailRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    public Optional<ContractDetail> findByContractId(@Param("contractId") Long contractId) {
        QContractDetail contractDetail = QContractDetail.contractDetail;
        QCleaning cleaning = QCleaning.cleaning;
        QContract contract = QContract.contract;
        QManager manager = QManager.manager;
        QCustomer customer = QCustomer.customer;

        return Optional.ofNullable(jpaQueryFactory
                .selectFrom(contractDetail)
                .join(contractDetail.contract, contract).fetchJoin()
                .join(contractDetail.contract.cleaning, cleaning).fetchJoin()
                .join(contractDetail.contract.manager, manager).fetchJoin()
                .join(contractDetail.contract.customer, customer).fetchJoin()
                .where(contractDetail.contract.id.eq((contractId)))
                .fetchOne());
    }
}
