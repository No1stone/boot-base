package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryRes;
import com.origemite.lib.model.auth.service.MemberLoginHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberLoginHistoryFacade {

    private final MemberLoginHistoryService memberLoginHistoryService;

    public Page<MemberLoginHistoryRes.Item> search(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        return memberLoginHistoryService.search(filter, pageable);
    }

    @Transactional
    public MemberLoginHistoryRes.Id save(MemberLoginHistoryReq.Create create) {
        return memberLoginHistoryService.save(create);
    }

    public MemberLoginHistoryRes.Item findById(Integer id) {
        return memberLoginHistoryService.findById(id);
    }

    @Transactional
    public MemberLoginHistoryRes.Id saveById(Integer id, MemberLoginHistoryReq.Update update) {
        return memberLoginHistoryService.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberLoginHistoryService.deleteById(id);
    }

    public List<MemberLoginHistoryRes.Item> findAll(MemberLoginHistoryReq.Filter filter) {
        return memberLoginHistoryService.findAll(filter);
    }

    public List<MemberLoginHistoryRes.Item> findByIds(List<Integer> ids) {
        return memberLoginHistoryService.findByIds(ids);
    }

    public Page<MemberLoginHistoryRes.Name> searchName(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        return memberLoginHistoryService.searchName(filter, pageable);
    }

    public List<MemberLoginHistoryRes.Name> findAllName(MemberLoginHistoryReq.Filter filter) {
        return memberLoginHistoryService.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberLoginHistoryService.deleteByIds(ids);
    }
}
