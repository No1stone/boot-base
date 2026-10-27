package com.origemite.lib.model.auth.persistence;

import com.origemite.lib.model.auth.dto.MemberOutReq;
import com.origemite.lib.model.auth.dto.MemberOutRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberOutPort {

    Page<MemberOutRes.Item> search(MemberOutReq.Filter filter, Pageable pageable);

    MemberOutRes.Id save(MemberOutReq.Create create);

    List<MemberOutRes.Id> saveAll(List<MemberOutReq.Create> createList);

    MemberOutRes.Item findById(Integer id);

    MemberOutRes.Id saveById(Integer id, MemberOutReq.Update update);

    void deleteById(Integer id);

    List<MemberOutRes.Item> findAll(MemberOutReq.Filter filter);

    List<MemberOutRes.Item> findByIds(List<Integer> ids);

    Page<MemberOutRes.Name> searchName(MemberOutReq.Filter filter, Pageable pageable);

    List<MemberOutRes.Name> findAllName(MemberOutReq.Filter filter);

    void deleteByIds(List<Integer> ids);
}
