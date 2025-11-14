package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.common.base.CreatedEntity;
import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.auth.entity.MemberLoginHistory;
import org.springframework.data.jpa.domain.Specification;

public class MemberLoginHistorySpecs {

    public static Specification<MemberLoginHistory> of(MemberLoginHistoryReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(MemberLoginHistory.Fields.id, filter.getId());
            predicateBuilder.equal(MemberLoginHistory.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginAt, filter.getLoginAt());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginHistoryUserAgent, filter.getLoginHistoryUserAgent());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginHistoryIp, filter.getLoginHistoryIp());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginSuccessYn, filter.getLoginSuccessYn());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());

            return predicateBuilder.build();
        };
    }
}
