package com.origemite.lib.model.member.persistence;

import com.origemite.lib.model.member.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.member.dto.MemberPasswordChangeHistoryRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberPasswordChangeHistoryPort {

    Page<MemberPasswordChangeHistoryRes.Item> search(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable);

    MemberPasswordChangeHistoryRes.Id save(MemberPasswordChangeHistoryReq.Create create);

    List<MemberPasswordChangeHistoryRes.Id> saveAll(List<MemberPasswordChangeHistoryReq.Create> createList);

    MemberPasswordChangeHistoryRes.Item findById(Integer id);

    MemberPasswordChangeHistoryRes.Id saveById(Integer id, MemberPasswordChangeHistoryReq.Update update);

    void deleteById(Integer id);

    List<MemberPasswordChangeHistoryRes.Item> findAll(MemberPasswordChangeHistoryReq.Filter filter);

    List<MemberPasswordChangeHistoryRes.Item> findByIds(List<Integer> ids);

    Page<MemberPasswordChangeHistoryRes.Name> searchName(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable);

    List<MemberPasswordChangeHistoryRes.Name> findAllName(MemberPasswordChangeHistoryReq.Filter filter);

    void deleteByIds(List<Integer> ids);
}
