package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.common.base.CreatedEntity;
import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.entity.Member;
import org.springframework.data.jpa.domain.Specification;

public class MemberSpecs {

    public static Specification<Member> of(MemberReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(Member.Fields.id, filter.getId());
            predicateBuilder.equal(Member.Fields.loginId, filter.getLoginId());
            predicateBuilder.equal(Member.Fields.loginPassword, filter.getLoginPassword());
            predicateBuilder.equal(Member.Fields.loginPasswordSaltKey, filter.getLoginPasswordSaltKey());
            predicateBuilder.equal(Member.Fields.status, filter.getStatus());
            // predicateBuilder.equal(UpdatedEntity.Fields.updatedBy, filter.getUpdatedBy());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());

            return predicateBuilder.build();
        };
    }
}
