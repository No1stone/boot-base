package com.origemite.lib.model.auth.repository;

import com.origemite.lib.model.auth.entity.AppMemberSession;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * AppMemberSession Repository
 */
public interface AppMemberSessionRepository extends JpaRepository<AppMemberSession, String>, AppMemberSessionCustomRepository , JpaSpecificationExecutor<AppMemberSession>{
    List<AppMemberSession> findByIdIn(List<String> ids);
    @Query("select a from AppMemberSession a where a.refreshToken = :refreshToken and a.expiration >= current_timestamp")
    AppMemberSession findActiveByRefreshToken(String refreshToken);
}

@Repository
interface AppMemberSessionCustomRepository {
}

@RequiredArgsConstructor
class AppMemberSessionCustomRepositoryImpl implements AppMemberSessionCustomRepository {
    private final JPAQueryFactory queryFactory;
}
