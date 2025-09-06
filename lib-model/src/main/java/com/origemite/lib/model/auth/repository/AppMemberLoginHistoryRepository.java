package com.origemite.lib.model.auth.repository;

import com.origemite.lib.model.auth.entity.AppMemberLoginHistory;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AppMemberLoginHistory Repository
 */
public interface AppMemberLoginHistoryRepository extends JpaRepository<AppMemberLoginHistory, Integer>, AppMemberLoginHistoryCustomRepository , JpaSpecificationExecutor<AppMemberLoginHistory>{
    List<AppMemberLoginHistory> findByIdIn(List<Integer> ids);
}

@Repository
interface AppMemberLoginHistoryCustomRepository {
}

@RequiredArgsConstructor
class AppMemberLoginHistoryCustomRepositoryImpl implements AppMemberLoginHistoryCustomRepository {
    private final JPAQueryFactory queryFactory;
}
