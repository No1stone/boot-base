package com.origemite.apiauth.member.facade;


import com.origemite.apiauth.auth.service.VaultService;
import com.origemite.lib.common.util.EncUtils;
import com.origemite.lib.common.util.Salt;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.model.auth.service.MemberService;
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
public class MemberFacade {

    private final MemberService memberService;
    private final VaultService vaultService;

    public Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable) {
        return memberService.search(filter, pageable);
    }

    @Transactional
    public MemberRes.Id save(MemberReq.Create create) {
        String salt = Salt.generate();
        create.setLoginPasswordSaltKey(salt);
        create.setLoginPassword(EncUtils.encSHA512(create.getLoginPassword(), salt));
        return memberService.save(create);
    }

    public MemberRes.Item findById(String id) {
        return memberService.findById(id);
    }

    @Transactional
    public MemberRes.Id saveById(String id, MemberReq.Update update) {
        return memberService.saveById(id, update);
    }

    @Transactional
    public void deleteById(String id) {
        memberService.deleteById(id);
    }

    public List<MemberRes.Item> findAll(MemberReq.Filter filter) {
        return memberService.findAll(filter);
    }

    public List<MemberRes.Item> findByIds(List<String> ids) {
        return memberService.findByIds(ids);
    }

    public Page<MemberRes.Name> searchName(MemberReq.Filter filter, Pageable pageable) {
        return memberService.searchName(filter, pageable);
    }

    public List<MemberRes.Name> findAllName(MemberReq.Filter filter) {
        return memberService.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<String> ids) {
        memberService.deleteByIds(ids);
    }


    @Transactional
    public void login(){}


    @Transactional
    public void refreshToken(){}


    @Transactional
    public void create(){}


    @Transactional
    public void update(){}

}
