package com.origemite.lib.model.auth.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.origemite.lib.model.auth.entity.MemberIdentification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * MemberIdentification Repository
 */
public interface MemberIdentificationRepository extends JpaRepository<MemberIdentification, Integer>, MemberIdentificationCustomRepository , JpaSpecificationExecutor<MemberIdentification>{
    List<MemberIdentification> findByIdIn(List<Integer> ids);
}

@Repository
interface MemberIdentificationCustomRepository {
}

@RequiredArgsConstructor
class MemberIdentificationCustomRepositoryImpl implements MemberIdentificationCustomRepository {
    private final JPAQueryFactory queryFactory;
}
