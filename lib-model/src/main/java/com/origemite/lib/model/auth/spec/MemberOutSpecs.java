package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.MemberOutReq;
import com.origemite.lib.model.auth.entity.MemberOut;
import org.springframework.data.jpa.domain.Specification;

public class MemberOutSpecs {

    public static Specification<MemberOut> of(MemberOutReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(MemberOut.Fields.id, filter.getId());
            predicateBuilder.equal(MemberOut.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(MemberOut.Fields.outAt, filter.getOutAt());
            predicateBuilder.equal(MemberOut.Fields.outReason, filter.getOutReason());
            // predicateBuilder.equal(UpdatedEntity.Fields.updatedBy, filter.getUpdatedBy());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());

            return predicateBuilder.build();
        };
    }
}
