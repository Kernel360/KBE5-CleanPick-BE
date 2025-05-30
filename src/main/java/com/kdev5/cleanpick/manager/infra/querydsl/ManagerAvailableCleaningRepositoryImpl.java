package com.kdev5.cleanpick.manager.infra.querydsl;

import com.kdev5.cleanpick.cleaning.domain.QCleaning;
import com.kdev5.cleanpick.manager.domain.QManagerAvailableCleaning;
import com.kdev5.cleanpick.manager.domain.QManagerAvailableRegion;
import com.kdev5.cleanpick.manager.domain.QRegion;
import com.kdev5.cleanpick.manager.infra.repository.ManagerAvailableCleaningRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ManagerAvailableCleaningRepositoryImpl implements ManagerAvailableCleaningRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<String>> loadCleanings(List<Long> managerIds) {

        QManagerAvailableCleaning mac = QManagerAvailableCleaning.managerAvailableCleaning;
        QCleaning cleaning = QCleaning.cleaning;

        List<Tuple> result = queryFactory
                .select(mac.manager.id, cleaning.serviceName)
                .from(mac)
                .join(mac.cleaning, cleaning)
                .where(mac.manager.id.in(managerIds))
                .fetch();

        return result.stream().collect(Collectors.groupingBy(
                tuple -> tuple.get(mac.manager.id),
                Collectors.mapping(tuple -> tuple.get(cleaning.serviceName), Collectors.toList())
        ));
    }
}
