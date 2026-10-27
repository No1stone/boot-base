package com.origemite.lib.model.member.persistence;

import com.origemite.lib.model.member.dto.MemberConnectReq;
import com.origemite.lib.model.member.dto.MemberConnectRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberConnectPort {

    Page<MemberConnectRes.Item> search(MemberConnectReq.Filter filter, Pageable pageable);

    MemberConnectRes.Id save(MemberConnectReq.Create create);

    List<MemberConnectRes.Id> saveAll(List<MemberConnectReq.Create> createList);

    MemberConnectRes.Item findById(Integer id);

    MemberConnectRes.Id saveById(Integer id, MemberConnectReq.Update update);

    void deleteById(Integer id);

    List<MemberConnectRes.Item> findAll(MemberConnectReq.Filter filter);

    List<MemberConnectRes.Item> findByIds(List<Integer> ids);

    Page<MemberConnectRes.Name> searchName(MemberConnectReq.Filter filter, Pageable pageable);

    List<MemberConnectRes.Name> findAllName(MemberConnectReq.Filter filter);

    void deleteByIds(List<Integer> ids);
}
