package com.origemite.apiauth.member.client;

import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;

import java.util.List;

/**
 * {@link com.origemite.apiauth.member.controller.MemberController} 를 서버투서버로 호출하는 outbound Port.
 * DB {@code MemberPort} 와 별개. 인증 헤더 없음.
 */
public interface MemberClientPort {

    MemberRes.Item findById(String id);

    MemberRes.Id save(MemberReq.Create create);

    List<MemberRes.Id> saveAll(List<MemberReq.Create> createList);
}
