package com.kdev5.cleanpick.manager.infra.querydsl;


import com.kdev5.cleanpick.cleaning.domain.QCleaning;
import com.kdev5.cleanpick.manager.domain.*;
import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;
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
import org.springframework.stereotype.Repository;

import static com.kdev5.cleanpick.review.domain.enumeration.ReviewType.TO_MANAGER;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ManagerRepositoryImpl implements ManagerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ManagerSearchResponseDto> searchManagers(
            String cleaning,
            String region,
            String keyword,
            SortType sortType,
            Pageable pageable
    ) {
        QManager manager = QManager.manager;
        QReview review = QReview.review;
        QManagerAvailableCleaning mac = QManagerAvailableCleaning.managerAvailableCleaning;
        QCleaning cleaningEntity = QCleaning.cleaning;
        QManagerAvailableRegion mar = QManagerAvailableRegion.managerAvailableRegion;
        QRegion regionEntity = QRegion.region;

        //기본 쿼리
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

        // 검색어 적용 쿼리 생성
        if (keyword != null && !keyword.isBlank()) {
            baseQuery.where(manager.name.containsIgnoreCase(keyword));
        }

        //정렬 타입 적용 쿼리 생성
        baseQuery.orderBy(getOrderSpecifier(sortType, review, manager));

        long total = baseQuery.fetchCount();

        List<Manager> managers = baseQuery
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        //매니저 id list 저장
        List<Long> managerIds = managers.stream().map(Manager::getId).toList();
        //매니저별 평균 평점
        Map<Long, Double> avgMap = new HashMap<>();
        //매니저별 리뷰 수
        Map<Long, Long> countMap = new HashMap<>();

        loadReviewStats(managerIds, avgMap, countMap);

        //매니저별 가능지역 저장
        Map<Long, List<String>> regionMap = loadManagerRegions(managerIds);
        //매니저별 가능서비스 저장
        Map<Long, List<String>> cleaningMap = loadManagerCleanings(managerIds);

        //엔티티 -> response dto 맵핑
        List<ManagerSearchResponseDto> content = managers.stream()
                .map(m -> new ManagerSearchResponseDto(
                        m.getId(),
                        m.getName(),
                        avgMap.getOrDefault(m.getId(), 0.0), //id에 해당하는 평점 리턴
                        countMap.getOrDefault(m.getId(), 0L), //id에 해당하는 리뷰수 리턴
                        m.getProfileImageUrl(),
                        m.getProfileMessage(),
                        extractRegionSummary(m.getId(), region, regionMap), //id에 해당하는 지역 외 n곳 string 리턴
                        cleaningMap.getOrDefault(m.getId(), List.of())
                ))
                .toList();

        return new PageImpl<>(content, pageable, total);
    }

    // 필터 적용된 매니저로 avgMap, countMap 저장
    private void loadReviewStats(List<Long> managerIds, Map<Long, Double> avgMap, Map<Long, Long> countMap) {
        QReview review = QReview.review;

        List<Tuple> result = queryFactory
                .select(review.manager.id, review.rating.avg(), review.count())
                .from(review)
                .where(
                        review.manager.id.in(managerIds) //필터 적용된 매니저id
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

    // 매니저별 가능 지역 리스트 리턴
    private Map<Long, List<String>> loadManagerRegions(List<Long> managerIds) {
        QManagerAvailableRegion mar = QManagerAvailableRegion.managerAvailableRegion;
        QRegion region = QRegion.region;

        // (매니저, 지역) tuple
        List<Tuple> result = queryFactory
                .select(mar.manager.id, region.name)
                .from(mar)
                .join(mar.region, region)
                .where(mar.manager.id.in(managerIds))
                .fetch();

        // <매니저 , 지역리스트> map
        return result.stream()
                .collect(Collectors.groupingBy(
                        tuple -> tuple.get(mar.manager.id),
                        Collectors.mapping(tuple -> tuple.get(region.name), Collectors.toList())
                ));
    }

    // 매니저별 가능 서비스 리턴
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

    // 매니저별 검색한 {region} 외 {n}곳 리턴
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

    // 정렬 기준에 따라 정렬 객체 생성
    private OrderSpecifier<?> getOrderSpecifier(SortType sortType, QReview review, QManager manager) {
        switch (sortType) {
            case STAR -> { // 별점순으로 정렬
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
            case REVIEW_COUNT -> { //리뷰많은순으로 정렬
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
            default -> {             //기본순: 추천순으로 정렬
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
