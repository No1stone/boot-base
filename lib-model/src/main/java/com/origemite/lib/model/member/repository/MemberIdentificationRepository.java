package com.origemite.lib.model.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.member.entity.MemberIdentification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MemberIdentification Repository
 */
public interface MemberIdentificationRepository extends JpaRepository<MemberIdentification, Integer>, MemberIdentificationCustomRepository , JpaSpecificationExecutor<MemberIdentification>{
}

@Repository
interface MemberIdentificationCustomRepository {
}

@RequiredArgsConstructor
class MemberIdentificationCustomRepositoryImpl implements MemberIdentificationCustomRepository {
    private final JPAQueryFactory queryFactory;
}
