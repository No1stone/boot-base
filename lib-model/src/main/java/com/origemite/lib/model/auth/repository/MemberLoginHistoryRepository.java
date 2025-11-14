package com.origemite.lib.model.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.auth.entity.MemberLoginHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MemberLoginHistory Repository
 */
public interface MemberLoginHistoryRepository extends JpaRepository<MemberLoginHistory, Integer>, MemberLoginHistoryCustomRepository , JpaSpecificationExecutor<MemberLoginHistory>{
    List<MemberLoginHistory> findByIdIn(List<Integer> ids);
}

@Repository
interface MemberLoginHistoryCustomRepository {
}

@RequiredArgsConstructor
class MemberLoginHistoryCustomRepositoryImpl implements MemberLoginHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;
}
