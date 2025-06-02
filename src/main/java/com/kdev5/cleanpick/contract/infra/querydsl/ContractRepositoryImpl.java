package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.common.util.PageableToOrderSpecifiersUtil;
import com.kdev5.cleanpick.contract.domain.Contract;
import com.kdev5.cleanpick.contract.domain.QContract;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.service.dto.request.ContractFilterStatus;
import com.kdev5.cleanpick.global.exception.ErrorCode;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.user.domain.exception.InvalidUserRoleException;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class ContractRepositoryImpl implements ContractRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Contract> findByFilter(Long userId, String role, ContractFilterStatus filter, Pageable pageable) {
        QContract contract = QContract.contract;

        BooleanBuilder predicate = new BooleanBuilder();


        if ("ROLE_USER".equals(role)) {
            predicate.and(contract.customer.id.eq(userId));
        } else if ("ROLE_MANAGER".equals(role)) {
            predicate.and(contract.manager.id.eq(userId));
        } else {
            throw new InvalidUserRoleException(ErrorCode.INVALID_ROLE);
        }

        if (ContractFilterStatus.WAITING_MATCH.equals(filter)) {
            predicate.and(contract.manager.isNull());
        } else if (ContractFilterStatus.MATCHED.equals(filter)) {
            predicate.and(contract.status.in(ContractStatus.NOT_STARTED, ContractStatus.IN_PROGRESS))
                    .and(contract.manager.isNotNull());
        } else if (ContractFilterStatus.COMPLETED.equals(filter)) {
            predicate.and(contract.status.in(ContractStatus.COMPLETED, ContractStatus.AWATING_PAYMENT, ContractStatus.PAID))
                    .and(contract.manager.isNotNull());
        }


        List<Contract> content = queryFactory
                .selectFrom(contract)
                .where(predicate)
                .orderBy(PageableToOrderSpecifiersUtil.toOrderSpecifiers(pageable, Contract.class, "contract").toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(contract.id)
                .from(contract)
                .where(predicate);


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    //TODO: 병합 후 deleted_at null 조건 추가
    @Override
    public List<Contract> findContractsByManagerAndStatusWithinDateRange(Manager manager, List<ContractStatus> statuses, LocalDateTime start, LocalDateTime end) {
        QContract contract = QContract.contract;

        return queryFactory
                .selectFrom(contract)
                .join(contract.manager).fetchJoin()
                .where(
                        contract.manager.eq(manager),
                        contract.contractDate.between(start, end),
                        statuses != null && !statuses.isEmpty() ? contract.status.in(statuses) : null
                )
                .fetch();
    }

    @Override
    public Integer sumMonthlyTotalPriceByManager(Manager manager, List<ContractStatus> statuses, LocalDateTime start, LocalDateTime end) {
        QContract contract = QContract.contract;

        return queryFactory
                .select(contract.totalPrice.sum())
                .from(contract)
                .where(
                        contract.manager.eq(manager),
                        contract.contractDate.between(start, end),
                        statuses != null && !statuses.isEmpty() ? contract.status.in(statuses) : null
                )
                .fetchOne();
    }


}

