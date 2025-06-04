package com.kdev5.cleanpick.review.Infra.querydsl;

import com.kdev5.cleanpick.manager.service.dto.response.ReadManagerResponseDto;
import com.kdev5.cleanpick.review.domain.QReview;
import com.kdev5.cleanpick.review.domain.enumeration.ReviewType;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.kdev5.cleanpick.manager.domain.QManager.manager;
import static com.kdev5.cleanpick.review.domain.QReview.review;
import static com.kdev5.cleanpick.review.domain.enumeration.ReviewType.TO_MANAGER;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Map<Long, Double> getAvgRatingForMangers(List<Long> managerIds) {

        QReview review = QReview.review;

        List<Tuple> result = queryFactory
                .select(review.manager.id, review.rating.avg())
                .from(review)
                .where(review.manager.id.in(managerIds)
                        .and(review.type.eq(TO_MANAGER)))
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

    @Override
    public Map<Long, Long> getReviewCountForManagers(List<Long> managerIds) {

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

    @Override
    public List<ReadManagerResponseDto> findManagerRatingsWithInfo(List<Long> managerIds) {
        return queryFactory
                .select(Projections.constructor(
                        ReadManagerResponseDto.class,
                        manager.id,
                        manager.name,
                        review.rating.avg(),
                        review.id.count(),
                        manager.profileImageUrl,
                        manager.profileMessage

                ))
                .from(review)
                .join(review.manager, manager)
                .where(
                        review.manager.id.in(managerIds),
                        review.type.eq(ReviewType.TO_MANAGER)
                )
                .groupBy(manager.id, manager.name, manager.profileImageUrl)
                .fetch();

    }

}
