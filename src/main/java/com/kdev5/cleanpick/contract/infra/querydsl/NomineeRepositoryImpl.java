package com.kdev5.cleanpick.contract.infra.querydsl;

import com.kdev5.cleanpick.contract.domain.Nominee;
import com.kdev5.cleanpick.contract.domain.QContract;
import com.kdev5.cleanpick.contract.domain.QNominee;
import com.kdev5.cleanpick.contract.domain.enumeration.MatchingStatus;
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
    public Page<Nominee> findRequestedMatching(Long managerId, boolean isPersonal, Pageable pageable) {
        List<Nominee> content = queryFactory
                .selectFrom(nominee)
                .join(nominee.contract, contract).fetchJoin()
                .join(contract.cleaning, cleaning).fetchJoin()
                .join(contract.customer, customer).fetchJoin()
                .where(
                        nominee.manager.id.eq(managerId),
                        nominee.status.eq(MatchingStatus.PENDING),
                        contract.personal.eq(isPersonal)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(nominee.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(nominee.count())
                .from(nominee)
                .join(nominee.contract, contract)
                .where(
                        nominee.manager.id.eq(managerId),
                        contract.personal.eq(isPersonal)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, Optional.ofNullable(total).orElse(0L));
    }
}
