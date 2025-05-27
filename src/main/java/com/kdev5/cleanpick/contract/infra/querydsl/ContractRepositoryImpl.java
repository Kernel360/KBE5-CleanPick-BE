package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.QContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Contract> findByFilter(Long customerId, ContractFilterStatus filter, Pageable pageable) {
        QContract contract = QContract.contract;

        BooleanExpression predicate = contract.customer.id.eq(customerId);
        if (ContractFilterStatus.WAITING_MATCH.equals(filter))
            predicate = predicate.and(contract.manager.isNull());
        else if (ContractFilterStatus.MATCHED.equals(filter))
            predicate = predicate.and(contract.status.eq(ContractStatus.작업전)).and(contract.manager.isNotNull());
        else predicate = predicate.and(contract.status.ne(ContractStatus.작업전)).and(contract.manager.isNotNull());

        List<Contract> content = queryFactory
                .selectFrom(contract)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(contract.id)
                .from(contract)
                .where(predicate);


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }
}

