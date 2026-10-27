package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.member.support.display.DisplayNameResolver;
import com.origemite.lib.model.member.dto.MemberConnectReq;
import com.origemite.lib.model.member.dto.MemberConnectRes;
import com.origemite.lib.model.member.persistence.MemberConnectPort;
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

    private final MemberConnectPort memberConnectPort;
    private final DisplayNameResolver displayNameResolver;

    public Page<MemberConnectRes.Item> search(MemberConnectReq.Filter filter, Pageable pageable) {
        Page<MemberConnectRes.Item> page = memberConnectPort.search(filter, pageable);
        applyAuditorNames(page.getContent());
        return page;
    }

    @Transactional
    public MemberConnectRes.Id save(MemberConnectReq.Create create) {
        return memberConnectPort.save(create);
    }

    public MemberConnectRes.Item findById(Integer id) {
        MemberConnectRes.Item item = memberConnectPort.findById(id);
        applyAuditorNames(List.of(item));
        return item;
    }

    @Transactional
    public MemberConnectRes.Id saveById(Integer id, MemberConnectReq.Update update) {
        return memberConnectPort.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberConnectPort.deleteById(id);
    }

    public List<MemberConnectRes.Item> findAll(MemberConnectReq.Filter filter) {
        List<MemberConnectRes.Item> items = memberConnectPort.findAll(filter);
        applyAuditorNames(items);
        return items;
    }

    public List<MemberConnectRes.Item> findByIds(List<Integer> ids) {
        List<MemberConnectRes.Item> items = memberConnectPort.findByIds(ids);
        applyAuditorNames(items);
        return items;
    }

    public Page<MemberConnectRes.Name> searchName(MemberConnectReq.Filter filter, Pageable pageable) {
        return memberConnectPort.searchName(filter, pageable);
    }

    public List<MemberConnectRes.Name> findAllName(MemberConnectReq.Filter filter) {
        return memberConnectPort.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberConnectPort.deleteByIds(ids);
    }

    private void applyAuditorNames(List<MemberConnectRes.Item> items) {
        displayNameResolver.applyAuditorDisplayNames(
                items,
                MemberConnectRes.Item::getCreatedBy,
                MemberConnectRes.Item::setCreatedBy,
                MemberConnectRes.Item::getUpdatedBy,
                MemberConnectRes.Item::setUpdatedBy
        );
    }
}
