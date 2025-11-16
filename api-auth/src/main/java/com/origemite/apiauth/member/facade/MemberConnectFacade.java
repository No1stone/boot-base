package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.auth.dto.MemberConnectReq;
import com.origemite.lib.model.auth.dto.MemberConnectRes;
import com.origemite.lib.model.auth.service.MemberConnectService;
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
public class MemberConnectFacade {

    private final MemberConnectService memberConnectService;

    public Page<MemberConnectRes.Item> search(MemberConnectReq.Filter filter, Pageable pageable) {
        return memberConnectService.search(filter, pageable);
    }

    @Transactional
    public MemberConnectRes.Id save(MemberConnectReq.Create create) {
        return memberConnectService.save(create);
    }

    public MemberConnectRes.Item findById(Integer id) {
        return memberConnectService.findById(id);
    }

    @Transactional
    public MemberConnectRes.Id saveById(Integer id, MemberConnectReq.Update update) {
        return memberConnectService.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberConnectService.deleteById(id);
    }

    public List<MemberConnectRes.Item> findAll(MemberConnectReq.Filter filter) {
        return memberConnectService.findAll(filter);
    }

    public List<MemberConnectRes.Item> findByIds(List<Integer> ids) {
        return memberConnectService.findByIds(ids);
    }

    public Page<MemberConnectRes.Name> searchName(MemberConnectReq.Filter filter, Pageable pageable) {
        return memberConnectService.searchName(filter, pageable);
    }

    public List<MemberConnectRes.Name> findAllName(MemberConnectReq.Filter filter) {
        return memberConnectService.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberConnectService.deleteByIds(ids);
    }
}
