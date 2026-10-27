package com.origemite.apiauth.member.facade;

import com.origemite.apiauth.support.display.DisplayNameResolver;
import com.origemite.lib.common.util.EncUtils;
import com.origemite.lib.common.util.Salt;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.model.auth.persistence.MemberPort;
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

    private final MemberPort memberPort;
    private final DisplayNameResolver displayNameResolver;

    public Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable) {
        Page<MemberRes.Item> page = memberPort.search(filter, pageable);
        applyAuditorNames(page.getContent());
        return page;
    }

    @Transactional
    public MemberRes.Id save(MemberReq.Create create) {
        String salt = Salt.generate();
        create.setLoginPasswordSaltKey(salt);
        create.setLoginPassword(EncUtils.encSHA512(create.getLoginPassword(), salt));
        return memberPort.save(create);
    }

    public MemberRes.Item findById(String id) {
        MemberRes.Item item = memberPort.findById(id);
        applyAuditorNames(List.of(item));
        return item;
    }

    @Transactional
    public MemberRes.Id saveById(String id, MemberReq.Update update) {
        return memberPort.saveById(id, update);
    }

    @Transactional
    public void deleteById(String id) {
        memberPort.deleteById(id);
    }

    public List<MemberRes.Item> findAll(MemberReq.Filter filter) {
        List<MemberRes.Item> items = memberPort.findAll(filter);
        applyAuditorNames(items);
        return items;
    }

    public List<MemberRes.Item> findByIds(List<String> ids) {
        List<MemberRes.Item> items = memberPort.findByIds(ids);
        applyAuditorNames(items);
        return items;
    }

    public Page<MemberRes.Name> searchName(MemberReq.Filter filter, Pageable pageable) {
        return memberPort.searchName(filter, pageable);
    }

    public List<MemberRes.Name> findAllName(MemberReq.Filter filter) {
        return memberPort.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<String> ids) {
        memberPort.deleteByIds(ids);
    }

    @Transactional
    public void login() {
    }

    @Transactional
    public void refreshToken() {
    }

    @Transactional
    public void create() {
    }

    @Transactional
    public void update() {
    }

    private void applyAuditorNames(List<MemberRes.Item> items) {
        displayNameResolver.applyAuditorDisplayNames(
                items,
                MemberRes.Item::getCreatedBy,
                MemberRes.Item::setCreatedBy,
                MemberRes.Item::getUpdatedBy,
                MemberRes.Item::setUpdatedBy
        );
    }
}
