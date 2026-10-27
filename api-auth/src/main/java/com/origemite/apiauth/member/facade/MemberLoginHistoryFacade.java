package com.origemite.apiauth.member.facade;

import com.origemite.apiauth.support.display.DisplayNameResolver;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryRes;
import com.origemite.lib.model.auth.persistence.MemberLoginHistoryPort;
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

    private final MemberLoginHistoryPort memberLoginHistoryPort;
    private final DisplayNameResolver displayNameResolver;

    public Page<MemberLoginHistoryRes.Item> search(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        Page<MemberLoginHistoryRes.Item> page = memberLoginHistoryPort.search(filter, pageable);
        applyAuditorNames(page.getContent());
        return page;
    }

    @Transactional
    public MemberLoginHistoryRes.Id save(MemberLoginHistoryReq.Create create) {
        return memberLoginHistoryPort.save(create);
    }

    public MemberLoginHistoryRes.Item findById(Integer id) {
        MemberLoginHistoryRes.Item item = memberLoginHistoryPort.findById(id);
        applyAuditorNames(List.of(item));
        return item;
    }

    @Transactional
    public MemberLoginHistoryRes.Id saveById(Integer id, MemberLoginHistoryReq.Update update) {
        return memberLoginHistoryPort.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberLoginHistoryPort.deleteById(id);
    }

    public List<MemberLoginHistoryRes.Item> findAll(MemberLoginHistoryReq.Filter filter) {
        List<MemberLoginHistoryRes.Item> items = memberLoginHistoryPort.findAll(filter);
        applyAuditorNames(items);
        return items;
    }

    public List<MemberLoginHistoryRes.Item> findByIds(List<Integer> ids) {
        List<MemberLoginHistoryRes.Item> items = memberLoginHistoryPort.findByIds(ids);
        applyAuditorNames(items);
        return items;
    }

    public Page<MemberLoginHistoryRes.Name> searchName(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        return memberLoginHistoryPort.searchName(filter, pageable);
    }

    public List<MemberLoginHistoryRes.Name> findAllName(MemberLoginHistoryReq.Filter filter) {
        return memberLoginHistoryPort.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberLoginHistoryPort.deleteByIds(ids);
    }

    private void applyAuditorNames(List<MemberLoginHistoryRes.Item> items) {
        displayNameResolver.applyCreatedByDisplayNames(
                items,
                MemberLoginHistoryRes.Item::getCreatedBy,
                MemberLoginHistoryRes.Item::setCreatedBy
        );
    }
}
