package com.origemite.lib.model.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.member.entity.MemberOut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MemberOut Repository
 */
public interface MemberOutRepository extends JpaRepository<MemberOut, Integer>, MemberOutCustomRepository , JpaSpecificationExecutor<MemberOut>{
}

@Repository
interface MemberOutCustomRepository {
}

@RequiredArgsConstructor
class MemberOutCustomRepositoryImpl implements MemberOutCustomRepository {
    private final JPAQueryFactory queryFactory;
}
