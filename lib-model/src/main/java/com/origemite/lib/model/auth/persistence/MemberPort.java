package com.origemite.lib.model.auth.persistence;

import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberPort {

    Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable);

    MemberRes.Id save(MemberReq.Create create);

    List<MemberRes.Id> saveAll(List<MemberReq.Create> createList);

    MemberRes.Item findById(String id);

    MemberRes.Id saveById(String id, MemberReq.Update update);

    void deleteById(String id);

    List<MemberRes.Item> findAll(MemberReq.Filter filter);

    List<MemberRes.Item> findByIds(List<String> ids);

    Page<MemberRes.Name> searchName(MemberReq.Filter filter, Pageable pageable);

    List<MemberRes.Name> findAllName(MemberReq.Filter filter);

    void deleteByIds(List<String> ids);
}
