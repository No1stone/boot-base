package com.origemite.lib.model.auth.spec;

import com.origemite.lib.common.base.UpdatedEntity;
import com.origemite.lib.common.base.CreatedEntity;
import com.origemite.lib.common.base.PredicateBuilder;
import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.entity.MemberIdentification;
import org.springframework.data.jpa.domain.Specification;

public class MemberIdentificationSpecs {

    public static Specification<MemberIdentification> of(MemberIdentificationReq.Filter filter) {
        return (root, query, builder) -> {

            PredicateBuilder predicateBuilder = new PredicateBuilder(builder, root);

            predicateBuilder.equal(MemberIdentification.Fields.id, filter.getId());
            predicateBuilder.equal(MemberIdentification.Fields.name, filter.getName());
            predicateBuilder.equal(MemberIdentification.Fields.mobilePhoneNumber, filter.getMobilePhoneNumber());
            predicateBuilder.equal(MemberIdentification.Fields.email, filter.getEmail());
            predicateBuilder.equal(MemberIdentification.Fields.ci, filter.getCi());
            predicateBuilder.equal(MemberIdentification.Fields.birthday, filter.getBirthday());
            predicateBuilder.equal(MemberIdentification.Fields.gender, filter.getGender());
            predicateBuilder.equal(MemberIdentification.Fields.foreignYn, filter.getForeignYn());
            predicateBuilder.equal(MemberIdentification.Fields.nameSha, filter.getNameSha());
            predicateBuilder.equal(MemberIdentification.Fields.mobilePhoneNumberSha, filter.getMobilePhoneNumberSha());
            predicateBuilder.equal(MemberIdentification.Fields.emailSha, filter.getEmailSha());
            predicateBuilder.equal(MemberIdentification.Fields.ciSha, filter.getCiSha());
            predicateBuilder.equal(MemberIdentification.Fields.cipherKeyId, filter.getCipherKeyId());
            predicateBuilder.equal(MemberIdentification.Fields.mobileCarrierCode, filter.getMobileCarrierCode());
            predicateBuilder.equal(MemberIdentification.Fields.memberId, filter.getMemberId());
            predicateBuilder.equal(MemberIdentification.Fields.status, filter.getStatus());
            // predicateBuilder.equal(UpdatedEntity.Fields.updatedBy, filter.getUpdatedBy());
            // predicateBuilder.goe(CreatedEntity.Fields.createdAt, filter.getStartAt());
            // predicateBuilder.loe(CreatedEntity.Fields.createdAt, filter.getEndAt());
            // predicateBuilder.equal(CreatedEntity.Fields.createdBy, filter.getCreatedBy());

            return predicateBuilder.build();
        };
    }
}
