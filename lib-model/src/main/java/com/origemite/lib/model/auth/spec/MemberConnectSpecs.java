package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.common.base.CreatedEntity;
import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.MemberConnectReq;
import com.origemite.lib.model.auth.entity.MemberConnect;
import org.springframework.data.jpa.domain.Specification;

public class MemberConnectSpecs {

    public static Specification<MemberConnect> of(MemberConnectReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(MemberConnect.Fields.id, filter.getId());
            predicateBuilder.equal(MemberConnect.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(MemberConnect.Fields.connectTypeCode, filter.getConnectTypeCode());
            predicateBuilder.equal(MemberConnect.Fields.connectedId, filter.getConnectedId());
            predicateBuilder.equal(MemberConnect.Fields.name, filter.getName());
            predicateBuilder.equal(MemberConnect.Fields.email, filter.getEmail());
            predicateBuilder.equal(MemberConnect.Fields.mobile, filter.getMobile());
            predicateBuilder.equal(MemberConnect.Fields.picture, filter.getPicture());
            predicateBuilder.equal(MemberConnect.Fields.gender, filter.getGender());
            predicateBuilder.equal(MemberConnect.Fields.age, filter.getAge());
            predicateBuilder.equal(MemberConnect.Fields.birthday, filter.getBirthday());
            predicateBuilder.equal(MemberConnect.Fields.birthyear, filter.getBirthyear());
            predicateBuilder.equal(MemberConnect.Fields.locale, filter.getLocale());
            predicateBuilder.equal(MemberConnect.Fields.cipherKeyId, filter.getCipherKeyId());
            predicateBuilder.equal(MemberConnect.Fields.status, filter.getStatus());
            // predicateBuilder.equal(UpdatedEntity.Fields.updatedBy, filter.getUpdatedBy());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());

            return predicateBuilder.build();
        };
    }
}
