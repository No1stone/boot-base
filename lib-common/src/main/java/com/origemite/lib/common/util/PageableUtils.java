package com.origemite.lib.common.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageableUtils {
    public static Pageable of(Pageable pageable, Sort.Order defaultSort) {
        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(defaultSort));
        }
        if( pageable.getSort().get().noneMatch( sort ->  sort.getProperty().equals(defaultSort.getProperty()))) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort().and(Sort.by(defaultSort)));
        }
        return pageable;
    }
}
