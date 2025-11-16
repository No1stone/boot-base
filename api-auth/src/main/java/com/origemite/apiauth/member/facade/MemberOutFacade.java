package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.auth.dto.MemberOutReq;
import com.origemite.lib.model.auth.dto.MemberOutRes;
import com.origemite.lib.model.auth.service.MemberOutService;
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
public class MemberOutFacade {

    private final MemberOutService memberOutService;

    public Page<MemberOutRes.Item> search(MemberOutReq.Filter filter, Pageable pageable) {
        return memberOutService.search(filter, pageable);
    }

    @Transactional
    public MemberOutRes.Id save(MemberOutReq.Create create) {
        return memberOutService.save(create);
    }

    public MemberOutRes.Item findById(Integer id) {
        return memberOutService.findById(id);
    }

    @Transactional
    public MemberOutRes.Id saveById(Integer id, MemberOutReq.Update update) {
        return memberOutService.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberOutService.deleteById(id);
    }

    public List<MemberOutRes.Item> findAll(MemberOutReq.Filter filter) {
        return memberOutService.findAll(filter);
    }

    public List<MemberOutRes.Item> findByIds(List<Integer> ids) {
        return memberOutService.findByIds(ids);
    }

    public Page<MemberOutRes.Name> searchName(MemberOutReq.Filter filter, Pageable pageable) {
        return memberOutService.searchName(filter, pageable);
    }

    public List<MemberOutRes.Name> findAllName(MemberOutReq.Filter filter) {
        return memberOutService.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberOutService.deleteByIds(ids);
    }
}
