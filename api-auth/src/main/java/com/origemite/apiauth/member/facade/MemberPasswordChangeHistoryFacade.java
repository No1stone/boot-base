package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryRes;
import com.origemite.lib.model.auth.service.MemberPasswordChangeHistoryService;
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
public class MemberPasswordChangeHistoryFacade {

    private final MemberPasswordChangeHistoryService memberPasswordChangeHistoryService;

    public Page<MemberPasswordChangeHistoryRes.Item> search(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        return memberPasswordChangeHistoryService.search(filter, pageable);
    }

    @Transactional
    public MemberPasswordChangeHistoryRes.Id save(MemberPasswordChangeHistoryReq.Create create) {
        return memberPasswordChangeHistoryService.save(create);
    }

    public MemberPasswordChangeHistoryRes.Item findById(Integer id) {
        return memberPasswordChangeHistoryService.findById(id);
    }

    @Transactional
    public MemberPasswordChangeHistoryRes.Id saveById(Integer id, MemberPasswordChangeHistoryReq.Update update) {
        return memberPasswordChangeHistoryService.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberPasswordChangeHistoryService.deleteById(id);
    }

    public List<MemberPasswordChangeHistoryRes.Item> findAll(MemberPasswordChangeHistoryReq.Filter filter) {
        return memberPasswordChangeHistoryService.findAll(filter);
    }

    public List<MemberPasswordChangeHistoryRes.Item> findByIds(List<Integer> ids) {
        return memberPasswordChangeHistoryService.findByIds(ids);
    }

    public Page<MemberPasswordChangeHistoryRes.Name> searchName(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        return memberPasswordChangeHistoryService.searchName(filter, pageable);
    }

    public List<MemberPasswordChangeHistoryRes.Name> findAllName(MemberPasswordChangeHistoryReq.Filter filter) {
        return memberPasswordChangeHistoryService.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberPasswordChangeHistoryService.deleteByIds(ids);
    }
}
