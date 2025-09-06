package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.AppMemberSessionReq;
import com.origemite.lib.model.auth.entity.AppMemberSession;
import org.springframework.data.jpa.domain.Specification;

public class AppMemberSessionSpecs {

    public static Specification<AppMemberSession> of(AppMemberSessionReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(AppMemberSession.Fields.id, filter.getId());
            predicateBuilder.equal(AppMemberSession.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(AppMemberSession.Fields.refreshToken, filter.getRefreshToken());
            predicateBuilder.equal(AppMemberSession.Fields.expiration, filter.getExpiration());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());
            // predicateBuilder.equal(UpdatedEntity.Fields.updatedBy, filter.getUpdatedBy());

            return predicateBuilder.build();
        };
    }
}
