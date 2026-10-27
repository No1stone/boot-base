package com.origemite.lib.model.member.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.member.entity.MemberPasswordChangeHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MemberPasswordChangeHistory Repository
 */
public interface MemberPasswordChangeHistoryRepository extends JpaRepository<MemberPasswordChangeHistory, Integer>, MemberPasswordChangeHistoryCustomRepository , JpaSpecificationExecutor<MemberPasswordChangeHistory>{
}

@Repository
interface MemberPasswordChangeHistoryCustomRepository {
}

@RequiredArgsConstructor
class MemberPasswordChangeHistoryCustomRepositoryImpl implements MemberPasswordChangeHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;
}
