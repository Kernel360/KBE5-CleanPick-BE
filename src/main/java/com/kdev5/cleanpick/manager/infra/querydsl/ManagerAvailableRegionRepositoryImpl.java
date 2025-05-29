package com.kdev5.cleanpick.manager.infra.querydsl;

import com.kdev5.cleanpick.manager.domain.QManagerAvailableRegion;
import com.kdev5.cleanpick.manager.domain.QRegion;
import com.kdev5.cleanpick.manager.infra.repository.ManagerAvailableRegionRepository;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ManagerAvailableRegionRepositoryImpl implements ManagerAvailableRegionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, List<String>> loadRegions(List<Long> managerIds) {

        QManagerAvailableRegion mar = QManagerAvailableRegion.managerAvailableRegion;
        QRegion region = QRegion.region;

        List<Tuple> result = queryFactory
                .select(mar.manager.id, region.name)
                .from(mar)
                .join(mar.region, region)
                .where(mar.manager.id.in(managerIds))
                .fetch();

        return result.stream().collect(Collectors.groupingBy(
                tuple -> tuple.get(mar.manager.id),
                Collectors.mapping(tuple -> tuple.get(region.name), Collectors.toList())
        ));
    }
}
