package com.origemite.lib.model.member.persistence;

import com.origemite.lib.model.member.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.member.dto.MemberLoginHistoryRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberLoginHistoryPort {

    Page<MemberLoginHistoryRes.Item> search(MemberLoginHistoryReq.Filter filter, Pageable pageable);

    MemberLoginHistoryRes.Id save(MemberLoginHistoryReq.Create create);

    List<MemberLoginHistoryRes.Id> saveAll(List<MemberLoginHistoryReq.Create> createList);

    MemberLoginHistoryRes.Item findById(Integer id);

    MemberLoginHistoryRes.Id saveById(Integer id, MemberLoginHistoryReq.Update update);

    void deleteById(Integer id);

    List<MemberLoginHistoryRes.Item> findAll(MemberLoginHistoryReq.Filter filter);

    List<MemberLoginHistoryRes.Item> findByIds(List<Integer> ids);

    Page<MemberLoginHistoryRes.Name> searchName(MemberLoginHistoryReq.Filter filter, Pageable pageable);

    List<MemberLoginHistoryRes.Name> findAllName(MemberLoginHistoryReq.Filter filter);

    void deleteByIds(List<Integer> ids);
}
