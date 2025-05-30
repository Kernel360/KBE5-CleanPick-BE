package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.cleaning.domain.enumeration.ServiceName;
import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.domain.QContract;
import com.kdev5.cleanpick.contract.domain.QNominee;
import com.kdev5.cleanpick.contract.domain.enumeration.ContractStatus;
import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.kdev5.cleanpick.cleaning.domain.QCleaning.cleaning;
import static com.kdev5.cleanpick.customer.domain.QCustomer.customer;
import static com.kdev5.cleanpick.manager.domain.QManager.manager;

@RequiredArgsConstructor
public class NomineeRepositoryImpl implements NomineeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QNominee nominee = QNominee.nominee;
    private final QContract contract = QContract.contract;

    @Override
    public Page<Nominee> findRequestedMatching(Long managerId, Boolean isPersonal, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(nominee.manager.id.eq(managerId))
                .and(nominee.status.eq(MatchingStatus.PENDING));

        if (isPersonal != null) builder.and(contract.personal.eq(isPersonal));

        return fetchNomineesWithPaging(builder, pageable, nominee.createdAt.desc());
    }

    @Override
    public Page<Nominee> readAcceptedMatching(Long managerId, ServiceName type, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(nominee.manager.id.eq(managerId))
                .and(nominee.status.eq(MatchingStatus.ACCEPT));

        if (type != null) builder.and(contract.cleaning.serviceName.eq(type));

        return fetchNomineesWithPaging(builder, pageable, nominee.createdAt.desc());
    }

    @Override
    public Page<Nominee> findConfirmedMatching(Long managerId, ServiceName serviceName, String sortType, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder()
                .and(nominee.status.eq(MatchingStatus.CONFIRMED))
                .and(nominee.manager.id.eq(managerId))
                .and(nominee.contract.status.in(ContractStatus.작업전, ContractStatus.작업중));

        if (serviceName != null) builder.and(contract.cleaning.serviceName.eq(serviceName));

        return fetchNomineesWithPaging(builder, pageable, getOrderSpecifier(sortType));
    }

    private OrderSpecifier<LocalDateTime> getOrderSpecifier(String sortType) {
        Order order = Order.DESC;

        if ("contract".equalsIgnoreCase(sortType)) {
            return new OrderSpecifier<>(order, contract.contractDate);
        } else {
            return new OrderSpecifier<>(order, nominee.updatedAt);
        }
    }

    private Page<Nominee> fetchNomineesWithPaging(BooleanBuilder builder, Pageable pageable, OrderSpecifier<?> orderSpecifier) {
        List<Long> nomineeIds = fetchNomineeIds(builder, pageable, orderSpecifier);

        if (nomineeIds.isEmpty()) {
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<Nominee> content = fetchNomineesByIds(nomineeIds, orderSpecifier);
        Long total = countNominees(builder);

        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }

    private List<Long> fetchNomineeIds(BooleanBuilder builder, Pageable pageable, OrderSpecifier<?> orderSpecifier) {
        return queryFactory
                .select(nominee.id)
                .from(nominee)
                .join(nominee.contract, contract)
                .where(builder)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private List<Nominee> fetchNomineesByIds(List<Long> nomineeIds, OrderSpecifier<?> orderSpecifier) {
        return queryFactory
                .selectFrom(nominee)
                .join(nominee.contract, contract).fetchJoin()
                .join(contract.cleaning, cleaning).fetchJoin()
                .join(contract.customer, customer).fetchJoin()
                .join(contract.manager, manager).fetchJoin()
                .where(nominee.id.in(nomineeIds))
                .orderBy(orderSpecifier)
                .fetch();
    }

    private Long countNominees(BooleanBuilder builder) {
        return queryFactory
                .select(nominee.count())
                .from(nominee)
                .join(nominee.contract, contract)
                .where(builder)
                .fetchOne();
    }
}
