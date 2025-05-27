package com.kdev5.cleanpick.manager.infra.querydsl;


import com.kdev5.cleanpick.cleaning.domain.QCleaning;
import com.kdev5.cleanpick.manager.domain.*;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerResponseDto;
import com.kdev5.cleanpick.review.domain.QReview;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static com.kdev5.cleanpick.review.domain.enumeration.ReviewType.TO_MANAGER;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ManagerResponseDto> searchManagers(
            String cleaning,
            String region,
            String keyword,
            String sortType,
            Pageable pageable
    ) {
        QManager manager = QManager.manager;
        QReview review = QReview.review;
        QManagerAvailableCleaning mac = QManagerAvailableCleaning.managerAvailableCleaning;
        QCleaning cleaningEntity = QCleaning.cleaning;
        QManagerAvailableRegion mar = QManagerAvailableRegion.managerAvailableRegion;
        QRegion regionEntity = QRegion.region;

        //기본 쿼리 생성
        JPQLQuery<Manager> baseQuery = queryFactory
                .selectFrom(manager)
                .distinct();

        //클리닝 필터 적용 쿼리 생성
        if (cleaning != null) {
            baseQuery
                    .join(mac).on(mac.manager.eq(manager))
                    .join(mac.cleaning, cleaningEntity)
                    .where(cleaningEntity.serviceName.eq(cleaning));
        }

        //지역 필터 적용 쿼리 생성
        if (region != null) {
            baseQuery
                    .join(mar).on(mar.manager.eq(manager))
                    .join(mar.region, regionEntity)
                    .where(regionEntity.name.eq(region));
        }

        //검색어 적용 쿼리 생성
        if (keyword != null && !keyword.isBlank()) {
            baseQuery.where(manager.name.containsIgnoreCase(keyword));
        }

        //정렬 타입 적용 쿼리 생성
        baseQuery.orderBy(getOrderSpecifier(sortType, review, manager));

        //페이징 위해 검색된 전체 row 수 저장
        long total = baseQuery.fetchCount();

        //페이징 처리
        List<Manager> managers = baseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> managerIds = managers.stream().map(Manager::getId).toList();

        Map<Long, Double> avgMap = new HashMap<>();
        Map<Long, Long> countMap = new HashMap<>();
        loadReviewStats(managerIds, avgMap, countMap);

        Map<Long, List<String>> regionMap = loadManagerRegions(managerIds);
        Map<Long, List<String>> cleaningMap = loadManagerCleanings(managerIds);

        //엔티티 -> response dto 맵핑
        List<ManagerResponseDto> content = managers.stream()
                .map(m -> new ManagerResponseDto(
                        m.getId(),
                        m.getName(),
                        avgMap.getOrDefault(m.getId(), 0.0),
                        countMap.getOrDefault(m.getId(), 0L),
                        m.getProfileImageUrl(),
                        m.getProfileMessage(),
                        extractRegionSummary(m.getId(), region, regionMap),
                        cleaningMap.getOrDefault(m.getId(), List.of())
                ))
                .toList();
        //dto + 페이징데이터 리턴
        return new PageImpl<>(content, pageable, total);
    }

    private void loadReviewStats(List<Long> managerIds, Map<Long, Double> avgMap, Map<Long, Long> countMap) {
        QReview review = QReview.review;

        List<Tuple> result = queryFactory
                .select(review.manager.id, review.rating.avg(), review.count())
                .from(review)
                .where(
                        review.manager.id.in(managerIds)
                                .and(review.type.eq(TO_MANAGER))
                )
                .groupBy(review.manager.id)
                .fetch();

        for (Tuple tuple : result) {
            Long id = tuple.get(review.manager.id);
            Double avg = tuple.get(review.rating.avg());
            Long cnt = tuple.get(review.count());
            avgMap.put(id, avg != null ? avg : 0.0);
            countMap.put(id, cnt != null ? cnt : 0L);
        }
    }

    private Map<Long, List<String>> loadManagerRegions(List<Long> managerIds) {
        QManagerAvailableRegion mar = QManagerAvailableRegion.managerAvailableRegion;
        QRegion region = QRegion.region;

        List<Tuple> result = queryFactory
                .select(mar.manager.id, region.name)
                .from(mar)
                .join(mar.region, region)
                .where(mar.manager.id.in(managerIds))
                .fetch();

        return result.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(mar.manager.id),
                        Collectors.mapping(tuple -> tuple.get(region.name), Collectors.toList())
                ));
    }

    private Map<Long, List<String>> loadManagerCleanings(List<Long> managerIds) {
        QManagerAvailableCleaning mac = QManagerAvailableCleaning.managerAvailableCleaning;
        QCleaning cleaning = QCleaning.cleaning;

        List<Tuple> result = queryFactory
                .select(mac.manager.id, cleaning.serviceName)
                .from(mac)
                .join(mac.cleaning, cleaning)
                .where(mac.manager.id.in(managerIds))
                .fetch();

        return result.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(mac.manager.id),
                        Collectors.mapping(tuple -> tuple.get(cleaning.serviceName), Collectors.toList())
                ));
    }

    private String extractRegionSummary(Long managerId, String regionFilter, Map<Long, List<String>> regionMap) {
        List<String> regionNames = regionMap.getOrDefault(managerId, List.of());

        if (regionFilter != null && regionNames.contains(regionFilter)) {
            long others = regionNames.stream()
                    .filter(name -> !name.equals(regionFilter))
                    .count();
            return others == 0 ? regionFilter : regionFilter + " 외 " + others + "곳";
        }

        return regionNames.isEmpty() ? "" : regionNames.get(0);
    }

    private OrderSpecifier<?> getOrderSpecifier(String sortType, QReview review, QManager manager) {
        switch (sortType) {
            case "평점순" -> {
                return new OrderSpecifier<>(
                        Order.DESC,
                        JPAExpressions.select(review.rating.avg().coalesce(0.0))
                                .from(review)
                                .where(
                                        review.manager.eq(manager)
                                                .and(review.type.eq(TO_MANAGER))
                                )
                );
            }
            case "리뷰많은순" -> {
                return new OrderSpecifier<>(
                        Order.DESC,
                        JPAExpressions.select(review.count().coalesce(0L))
                                .from(review)
                                .where(
                                        review.manager.eq(manager)
                                                .and(review.type.eq(TO_MANAGER))
                                )
                );
            }
            default -> {
                NumberExpression<Double> score = review.rating.avg().coalesce(0.0).multiply(0.7)
                        .add(review.count().doubleValue().multiply(0.3));
                return new OrderSpecifier<>(
                        Order.DESC,
                        JPAExpressions.select(score)
                                .from(review)
                                .where(
                                        review.manager.eq(manager)
                                                .and(review.type.eq(TO_MANAGER))
                                )
                );
            }
        }
    }
}
