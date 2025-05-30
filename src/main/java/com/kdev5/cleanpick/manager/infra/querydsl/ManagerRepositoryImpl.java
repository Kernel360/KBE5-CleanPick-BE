package com.kdev5.cleanpick.manager.infra.querydsl;


import com.kdev5.cleanpick.cleaning.domain.QCleaning;
import com.kdev5.cleanpick.manager.domain.*;
import com.kdev5.cleanpick.manager.domain.enumeration.SortType;
import com.kdev5.cleanpick.review.domain.QReview;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kdev5.cleanpick.review.domain.enumeration.ReviewType.TO_MANAGER;

@RequiredArgsConstructor
@Repository
public class ManagerRepositoryImpl implements ManagerRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Manager> findManagersByFilter(String cleaning, String region, String keyword, SortType sortType, Pageable pageable) {

        QManager manager = QManager.manager;
        QManagerAvailableCleaning mac = QManagerAvailableCleaning.managerAvailableCleaning;
        QCleaning cleaningEntity = QCleaning.cleaning;
        QManagerAvailableRegion mar = QManagerAvailableRegion.managerAvailableRegion;
        QRegion regionEntity = QRegion.region;
        QReview review = QReview.review;

        JPQLQuery<Manager> query = queryFactory
                .selectFrom(manager)
                .distinct();

        if (cleaning != null) {
            query.join(mac).on(mac.manager.eq(manager))
                    .join(mac.cleaning, cleaningEntity)
                    .where(cleaningEntity.serviceName.eq(cleaning));
        }

        if (region != null) {
            query.join(mar).on(mar.manager.eq(manager))
                    .join(mar.region, regionEntity)
                    .where(regionEntity.name.eq(region));
        }

        if (keyword != null && !keyword.isBlank()) {
            query.where(manager.name.containsIgnoreCase(keyword));
        }

        query.orderBy(getOrderSpecifier(sortType, review, manager));

        List<Manager> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPQLQuery<Long> countQuery = query
                .select(manager.countDistinct())
                .from(manager);


        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private OrderSpecifier<?> getOrderSpecifier(SortType sortType, QReview review, QManager manager) {
        switch (sortType) {
            case STAR -> {
                return new OrderSpecifier<>(Order.DESC,
                        JPAExpressions.select(review.rating.avg().coalesce(0.0))
                                .from(review)
                                .where(review.manager.eq(manager).and(review.type.eq(TO_MANAGER)))
                );
            }
            case REVIEW_COUNT -> {
                return new OrderSpecifier<>(Order.DESC,
                        JPAExpressions.select(review.count().coalesce(0L))
                                .from(review)
                                .where(review.manager.eq(manager).and(review.type.eq(TO_MANAGER)))
                );
            }
            default -> {
                NumberExpression<Double> score = review.rating.avg().coalesce(0.0).multiply(0.7)
                        .add(review.count().doubleValue().multiply(0.3));
                return new OrderSpecifier<>(Order.DESC,
                        JPAExpressions.select(score)
                                .from(review)
                                .where(review.manager.eq(manager).and(review.type.eq(TO_MANAGER)))
                );
            }
        }
    }
}
