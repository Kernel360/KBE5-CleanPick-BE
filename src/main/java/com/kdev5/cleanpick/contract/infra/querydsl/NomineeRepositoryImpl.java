package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.domain.QContract;
import com.kdev5.cleanpick.contract.domain.QNominee;
import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.kdev5.cleanpick.cleaning.domain.QCleaning.cleaning;
import static com.kdev5.cleanpick.customer.domain.QCustomer.customer;

@RequiredArgsConstructor
public class NomineeRepositoryImpl implements NomineeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private final QNominee nominee = QNominee.nominee;
    private final QContract contract = QContract.contract;

    @Override
    public Page<Nominee> findRequestedMatching(Long managerId, Boolean isPersonal, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(nominee.manager.id.eq(managerId));
        builder.and(nominee.status.eq(MatchingStatus.PENDING));

        if (isPersonal != null) builder.and(contract.personal.eq(isPersonal));

        List<Nominee> content = queryFactory
                .selectFrom(nominee)
                .join(nominee.contract, contract).fetchJoin()
                .join(contract.cleaning, cleaning).fetchJoin()
                .join(contract.customer, customer).fetchJoin()
                .where(builder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(nominee.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(nominee.count())
                .from(nominee)
                .join(nominee.contract, contract)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
