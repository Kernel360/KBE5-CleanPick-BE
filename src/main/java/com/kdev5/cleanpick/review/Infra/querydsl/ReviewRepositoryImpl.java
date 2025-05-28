package com.kdev5.cleanpick.review.Infra.querydsl;

import com.kdev5.cleanpick.review.domain.QReview;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
}
