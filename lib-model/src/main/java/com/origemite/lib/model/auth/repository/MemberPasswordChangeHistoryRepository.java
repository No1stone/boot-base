package com.origemite.lib.model.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.auth.entity.MemberPasswordChangeHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MemberPasswordChangeHistory Repository
 */
public interface MemberPasswordChangeHistoryRepository extends JpaRepository<MemberPasswordChangeHistory, Integer>, MemberPasswordChangeHistoryCustomRepository , JpaSpecificationExecutor<MemberPasswordChangeHistory>{
    List<MemberPasswordChangeHistory> findByIdIn(List<Integer> ids);
}

@Repository
interface MemberPasswordChangeHistoryCustomRepository {
}

@RequiredArgsConstructor
class MemberPasswordChangeHistoryCustomRepositoryImpl implements MemberPasswordChangeHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;
}
