package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.service.MemberIdentificationService;
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
public class MemberIdentificationFacade {

    private final MemberIdentificationService memberIdentificationService;

    public Page<MemberIdentificationRes.Item> search(MemberIdentificationReq.Filter filter, Pageable pageable) {
        return memberIdentificationService.search(filter, pageable);
    }

    @Transactional
    public MemberIdentificationRes.Id save(MemberIdentificationReq.Create create) {
        return memberIdentificationService.save(create);
    }

    public MemberIdentificationRes.Item findById(Integer id) {
        return memberIdentificationService.findById(id);
    }

    @Transactional
    public MemberIdentificationRes.Id saveById(Integer id, MemberIdentificationReq.Update update) {
        return memberIdentificationService.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberIdentificationService.deleteById(id);
    }

    public List<MemberIdentificationRes.Item> findAll(MemberIdentificationReq.Filter filter) {
        return memberIdentificationService.findAll(filter);
    }

    public List<MemberIdentificationRes.Item> findByIds(List<Integer> ids) {
        return memberIdentificationService.findByIds(ids);
    }

    public Page<MemberIdentificationRes.Name> searchName(MemberIdentificationReq.Filter filter, Pageable pageable) {
        return memberIdentificationService.searchName(filter, pageable);
    }

    public List<MemberIdentificationRes.Name> findAllName(MemberIdentificationReq.Filter filter) {
        return memberIdentificationService.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberIdentificationService.deleteByIds(ids);
    }
}
