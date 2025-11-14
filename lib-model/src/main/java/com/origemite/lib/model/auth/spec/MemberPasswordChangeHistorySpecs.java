package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.common.base.CreatedEntity;
import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.entity.MemberPasswordChangeHistory;
import org.springframework.data.jpa.domain.Specification;

public class MemberPasswordChangeHistorySpecs {

    public static Specification<MemberPasswordChangeHistory> of(MemberPasswordChangeHistoryReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(MemberPasswordChangeHistory.Fields.id, filter.getId());
            predicateBuilder.equal(MemberPasswordChangeHistory.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(MemberPasswordChangeHistory.Fields.loginPassword, filter.getLoginPassword());
            predicateBuilder.equal(MemberPasswordChangeHistory.Fields.loginPasswordSaltKey, filter.getLoginPasswordSaltKey());
            predicateBuilder.equal(MemberPasswordChangeHistory.Fields.changeAt, filter.getChangeAt());
            predicateBuilder.equal(MemberPasswordChangeHistory.Fields.cipherKeyId, filter.getCipherKeyId());
            // predicateBuilder.equal(UpdatedEntity.Fields.updatedBy, filter.getUpdatedBy());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());

            return predicateBuilder.build();
        };
    }
}
