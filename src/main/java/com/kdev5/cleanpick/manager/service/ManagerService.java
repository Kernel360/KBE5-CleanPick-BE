package com.kdev5.cleanpick.manager.service;

import com.kdev5.cleanpick.cleaning.domain.QCleaning;
import com.kdev5.cleanpick.manager.domain.Manager;
import com.kdev5.cleanpick.manager.domain.QManagerAvailableCleaning;
import com.kdev5.cleanpick.manager.domain.QManagerAvailableRegion;
import com.kdev5.cleanpick.manager.domain.QRegion;
import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.manager.infra.repository.ManagerRepository;
import com.kdev5.cleanpick.manager.service.dto.response.ManagerSearchResponseDto;
import com.kdev5.cleanpick.review.domain.QReview;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kdev5.cleanpick.review.domain.enumeration.ReviewType.TO_MANAGER;

@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;
    private final JPAQueryFactory queryFactory;

    public Page<ManagerSearchResponseDto> searchManagers(
            String cleaning,
            String region,
            String keyword,
            SortType sortType,
            Pageable pageable
    ) {
        Page<Manager> filteredManagers = managerRepository.findManagersByFilter(cleaning, region, keyword, sortType, pageable);

        List<Long> managerIds = filteredManagers.stream().map(Manager::getId).toList();

        Map<Long, Double> avgMap = loadAvgRating(managerIds);
        Map<Long, Long> countMap = loadReviewCount(managerIds);
        Map<Long, List<String>> regionMap = loadRegions(managerIds);
        Map<Long, List<String>> cleaningMap = loadCleanings(managerIds);

        List<ManagerSearchResponseDto> content = filteredManagers.stream()
                .map(m -> new ManagerSearchResponseDto(
                        m.getId(),
                        m.getName(),
                        avgMap.getOrDefault(m.getId(), 0.0),
                        countMap.getOrDefault(m.getId(), 0L),
                        m.getProfileImageUrl(),
                        m.getProfileMessage(),
                        extractRegionSummary(m.getId(), region, regionMap),
                        cleaningMap.get(m.getId())
                )).toList();

        return new PageImpl<>(content, pageable, filteredManagers.getTotalElements());
    }

    private Map<Long, Double> loadAvgRating(List<Long> managerIds) {
        QReview review = QReview.review;
        List<Tuple> result = queryFactory
                .select(review.manager.id, review.rating.avg())
                .from(review)
                .where(review.manager.id.in(managerIds).and(review.type.eq(TO_MANAGER)))
                .groupBy(review.manager.id)
                .fetch();

        return result.stream().collect(Collectors.toMap(
                tuple -> tuple.get(review.manager.id),
                tuple -> {
                    Double rating = tuple.get(review.rating.avg());
                    return rating != null ? rating : 0.0;
                }
        ));
    }

    private Map<Long, Long> loadReviewCount(List<Long> managerIds) {
        QReview review = QReview.review;
        List<Tuple> result = queryFactory
                .select(review.manager.id, review.count())
                .from(review)
                .where(review.manager.id.in(managerIds).and(review.type.eq(TO_MANAGER)))
                .groupBy(review.manager.id)
                .fetch();

        return result.stream().collect(Collectors.toMap(
                tuple -> tuple.get(review.manager.id),
                tuple -> {
                    Long count = tuple.get(review.count());
                    return count != null ? count : 0L;
                }
        ));
    }

    private Map<Long, List<String>> loadRegions(List<Long> managerIds) {
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

    private Map<Long, List<String>> loadCleanings(List<Long> managerIds) {
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

    private String extractRegionSummary(Long managerId, String regionFilter, Map<Long, List<String>> regionMap) {
        List<String> regionNames = regionMap.getOrDefault(managerId, List.of());

        if (regionNames.isEmpty()) return "";

        if (regionFilter != null && regionNames.contains(regionFilter)) {
            long others = regionNames.stream()
                    .filter(name -> !name.equals(regionFilter))
                    .count();
            return others == 0 ? regionFilter : regionFilter + " 외 " + others + "곳";
        }

        String firstRegion = regionNames.get(0);
        long others = regionNames.size() - 1;

        return others == 0 ? firstRegion : firstRegion + " 외 " + others + "곳";
    }

}

