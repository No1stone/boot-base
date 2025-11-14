package com.origemite.lib.model.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.auth.entity.MemberOut;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MemberOut Repository
 */
public interface MemberOutRepository extends JpaRepository<MemberOut, Integer>, MemberOutCustomRepository , JpaSpecificationExecutor<MemberOut>{
    List<MemberOut> findByIdIn(List<Integer> ids);
}

@Repository
interface MemberOutCustomRepository {
}

@RequiredArgsConstructor
class MemberOutCustomRepositoryImpl implements MemberOutCustomRepository {
    private final JPAQueryFactory queryFactory;
}
