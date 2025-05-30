package com.kdev5.cleanpick.common.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;

public class PageableToOrderSpecifiersUtil {

    public static <T extends Comparable<?>> List<OrderSpecifier<?>> toOrderSpecifiers(Pageable pageable, Class<?> clazz, String alias) {
        PathBuilder<?> entityPath = new PathBuilder<>(clazz, alias);

        return pageable.getSort().stream()
                .map(order -> new OrderSpecifier<>(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        entityPath.get(order.getProperty(), Comparable.class)
                ))
                .collect(Collectors.toList());
    }
}
