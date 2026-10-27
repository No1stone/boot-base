package com.origemite.lib.model.member.spec;

import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.common.base.CreatedEntity;
import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.member.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.member.entity.MemberLoginHistory;
import org.springframework.data.jpa.domain.Specification;

public class MemberLoginHistorySpecs {

    public static Specification<MemberLoginHistory> of(MemberLoginHistoryReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(MemberLoginHistory.Fields.id, filter.getId());
            predicateBuilder.in(MemberLoginHistory.Fields.id, filter.getIds());
            predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            predicateBuilder.equal(MemberLoginHistory.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginAt, filter.getLoginAt());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginHistoryUserAgent, filter.getLoginHistoryUserAgent());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginHistoryIp, filter.getLoginHistoryIp());
            predicateBuilder.equal(MemberLoginHistory.Fields.loginSuccessYn, filter.getLoginSuccessYn());

            return predicateBuilder.build();
        };
    }
}
