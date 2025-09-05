package com.origemite.lib.common.base;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.*;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public class CustomBooleanBuilder {
    private BooleanBuilder builder = new BooleanBuilder();

    public static CustomBooleanBuilder builder() {
        return new CustomBooleanBuilder();
    }

    public <T extends Comparable<?>> CustomBooleanBuilder ifNotEmpty(Function<T, BooleanExpression> fn, T value) {
        return ifNotEmpty(fn, value, null);
    }

    public <T extends Comparable<?>> CustomBooleanBuilder ifNotEmpty(Function<T, BooleanExpression> fn, T value, T defaultValue) {
        if (!ObjectUtils.isEmpty(value)) {
            builder.and(fn.apply(value));
        } else if (!ObjectUtils.isEmpty(defaultValue)) {
            builder.and(fn.apply(defaultValue));
        }
        return this;
    }

//    public <T extends Comparable<?>> CustomBooleanBuilder ifNotEmpty(Function<T, BooleanExpression> fn, T valueA, T valueB, T booleanT) {
//        if (!ObjectUtils.isEmpty(valueA) & !ObjectUtils.isEmpty(valueB)) {
//            builder.and(fn.apply(valueA, valueB));
//        }
//
//        return this;
//    }

    public <T extends Comparable<?>> CustomBooleanBuilder ifNotEmpty(Function<List<T>, BooleanExpression> fn, List<T> value) {
        return ifNotEmpty(fn, value, null);
    }

    public <T extends Comparable<?>> CustomBooleanBuilder ifNotEmpty(Function<List<T>, BooleanExpression> fn, List<T> value, List<T> defaultValue) {
        if (!ObjectUtils.isEmpty(value)) {
            builder.and(fn.apply(value));
        } else if (!ObjectUtils.isEmpty(defaultValue)) {
            builder.and(fn.apply(defaultValue));
        }
        return this;
    }

//    public PredicateBuilder bitOr(String property, Integer value) {
//        if (value != null) {
//            Path<Integer> path = root.get(property);
//            Expression<Integer> expression = criteriaBuilder.function("bitor", Integer.class, path, criteriaBuilder.literal(value));
//            predicates.add(criteriaBuilder.equal(expression, value));
//        }
//        return this;
//    }

    // bitOr 메소드 추가 위의 코드 참고해서 builder를 이용해서
    public CustomBooleanBuilder bitAnd(NumberPath<Integer> path, Integer value) {
        if (value != null) {
//            BooleanOperation predicate = Expressions.predicate(Ops.GT, path, Expressions.numberOperation(Integer.class, Ops.AND, Expressions.asNumber(path), Expressions.asNumber(value)), Expressions.asNumber(0));
//            builder.and(predicate.isTrue());
            BooleanTemplate booleanTemplate = Expressions.booleanTemplate("BITAND({0}, {1}) > 0", path, value);
            builder.and(booleanTemplate);
        }
        return this;
    }

    public CustomBooleanBuilder ifNotEmpty(StringPath path, BiFunction<StringExpression, String, BooleanExpression> fn, String value) {
        if(!ObjectUtils.isEmpty(value)) builder.and(fn.apply(path, value));
        return this;
    }

    public <T extends Comparable<?>> CustomBooleanBuilder and(Function<T, BooleanExpression> fn, T value) {
        builder.and(fn.apply(value));
        return this;
    }

    public BooleanBuilder build() {
        return builder;
    }

}
