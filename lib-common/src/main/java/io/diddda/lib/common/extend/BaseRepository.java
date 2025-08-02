package io.diddda.lib.common.extend;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class BaseRepository {

    public static OrderSpecifier<?>[] order(OrderSpecifier<?>... order) {
        return order;
    }


    public static <T> PageImpl<T> page(List<T> content, Pageable pageable, long totalSupplier) {
        return new PageImpl<>(content, pageable, totalSupplier);
    }

    @SuppressWarnings("rawtypes")
    public static OrderSpecifier[] order(Pageable pageable) {
        return order(pageable, null);
    }

    @SuppressWarnings("rawtypes")
    public static OrderSpecifier[] order(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        for (Sort.Order order : sort) {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            orders.add(makeOrderSpecifier(direction, order.getProperty(), getNullHandling(order)));
        }

        return orders.toArray(OrderSpecifier[]::new);
    }

    @SuppressWarnings("rawtypes")
    public static OrderSpecifier[] order(List<Sort.Order> defaultSorts) {
        return order(null, defaultSorts);
    }

    @SuppressWarnings("rawtypes")
    public static OrderSpecifier[] order(Pageable pageable, List<Sort.Order> defaultSorts) {
        List<OrderSpecifier> orders = new ArrayList<>();

        if (pageable != null && pageable.getSort() != Sort.unsorted()) {
            for (Sort.Order order : pageable.getSort()) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                orders.add(makeOrderSpecifier(direction, order.getProperty(), getNullHandling(order)));
            }

            defaultSorts = defaultSorts.stream()
                    .filter(order ->
                            pageable.getSort().stream()
                                    .noneMatch(p -> p.getProperty().equals(order.getProperty())))
                    .toList();
        }

        if (defaultSorts != null && !defaultSorts.isEmpty()) {
            for (Sort.Order order : defaultSorts) {
                Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
                orders.add(makeOrderSpecifier(direction, order.getProperty(), getNullHandling(order)));
            }
        }

        return orders.toArray(OrderSpecifier[]::new);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static OrderSpecifier makeOrderSpecifier(Order order, String fieldName, OrderSpecifier.NullHandling nullHandling) {
        Path<Object> fieldPath = Expressions.path(Object.class, fieldName);
        return new OrderSpecifier(order, fieldPath, nullHandling);
    }

    private static OrderSpecifier.NullHandling getNullHandling(Sort.Order order) {
        OrderSpecifier.NullHandling nullHandling = OrderSpecifier.NullHandling.Default;
        Sort.NullHandling handling = order.getNullHandling();
        if (handling == Sort.NullHandling.NULLS_LAST) {
            return OrderSpecifier.NullHandling.NullsLast;
        } else if (handling == Sort.NullHandling.NULLS_FIRST) {
            return OrderSpecifier.NullHandling.NullsFirst;
        }
        return nullHandling;
    }

}
