package com.origemite.lib.model.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.auth.entity.MemberConnect;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MemberConnect Repository
 */
public interface MemberConnectRepository extends JpaRepository<MemberConnect, Integer>, MemberConnectCustomRepository , JpaSpecificationExecutor<MemberConnect>{
    List<MemberConnect> findByIdIn(List<Integer> ids);
}

@Repository
interface MemberConnectCustomRepository {
}

@RequiredArgsConstructor
class MemberConnectCustomRepositoryImpl implements MemberConnectCustomRepository {
    private final JPAQueryFactory queryFactory;
}
