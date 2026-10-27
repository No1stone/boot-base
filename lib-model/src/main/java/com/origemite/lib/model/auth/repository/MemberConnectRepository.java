package com.origemite.lib.model.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.auth.entity.MemberConnect;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * MemberConnect Repository
 */
public interface MemberConnectRepository extends JpaRepository<MemberConnect, Integer>, MemberConnectCustomRepository , JpaSpecificationExecutor<MemberConnect>{
}

@Repository
interface MemberConnectCustomRepository {
}

@RequiredArgsConstructor
class MemberConnectCustomRepositoryImpl implements MemberConnectCustomRepository {
    private final JPAQueryFactory queryFactory;
}
