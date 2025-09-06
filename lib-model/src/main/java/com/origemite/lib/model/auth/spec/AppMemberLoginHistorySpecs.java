package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.AppMemberLoginHistoryReq;
import com.origemite.lib.model.auth.entity.AppMemberLoginHistory;
import org.springframework.data.jpa.domain.Specification;

public class AppMemberLoginHistorySpecs {

    public static Specification<AppMemberLoginHistory> of(AppMemberLoginHistoryReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(AppMemberLoginHistory.Fields.id, filter.getId());
            predicateBuilder.equal(AppMemberLoginHistory.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(AppMemberLoginHistory.Fields.loginAt, filter.getLoginAt());
            predicateBuilder.equal(AppMemberLoginHistory.Fields.loginHistoryUserAgent, filter.getLoginHistoryUserAgent());
            predicateBuilder.equal(AppMemberLoginHistory.Fields.loginHistoryIp, filter.getLoginHistoryIp());
            predicateBuilder.equal(AppMemberLoginHistory.Fields.loginSuccessYn, filter.getLoginSuccessYn());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());

            return predicateBuilder.build();
        };
    }
}
