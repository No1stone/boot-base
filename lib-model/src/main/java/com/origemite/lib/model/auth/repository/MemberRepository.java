package com.origemite.lib.model.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.auth.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Member Repository
 */
public interface MemberRepository extends JpaRepository<Member, String>, MemberCustomRepository , JpaSpecificationExecutor<Member>{
}

@Repository
interface MemberCustomRepository {
}

@RequiredArgsConstructor
class MemberCustomRepositoryImpl implements MemberCustomRepository {
    private final JPAQueryFactory queryFactory;
}
