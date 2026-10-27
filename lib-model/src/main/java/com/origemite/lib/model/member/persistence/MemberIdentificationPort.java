package com.origemite.lib.model.member.persistence;

import com.origemite.lib.model.member.dto.MemberIdentificationReq;
import com.origemite.lib.model.member.dto.MemberIdentificationRes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberIdentificationPort {

    Page<MemberIdentificationRes.Item> search(MemberIdentificationReq.Filter filter, Pageable pageable);

    MemberIdentificationRes.Id save(MemberIdentificationReq.Create create);

    List<MemberIdentificationRes.Id> saveAll(List<MemberIdentificationReq.Create> createList);

    MemberIdentificationRes.Item findById(Integer id);

    MemberIdentificationRes.Id saveById(Integer id, MemberIdentificationReq.Update update);

    void deleteById(Integer id);

    List<MemberIdentificationRes.Item> findAll(MemberIdentificationReq.Filter filter);

    List<MemberIdentificationRes.Item> findByIds(List<Integer> ids);

    Page<MemberIdentificationRes.Name> searchName(MemberIdentificationReq.Filter filter, Pageable pageable);

    List<MemberIdentificationRes.Name> findAllName(MemberIdentificationReq.Filter filter);

    void deleteByIds(List<Integer> ids);
}
