package com.origemite.apiauth.member.facade;

import com.origemite.apiauth.support.display.DisplayNameResolver;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryRes;
import com.origemite.lib.model.auth.persistence.MemberPasswordChangeHistoryPort;
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

    private final MemberPasswordChangeHistoryPort memberPasswordChangeHistoryPort;
    private final DisplayNameResolver displayNameResolver;

    public Page<MemberPasswordChangeHistoryRes.Item> search(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        Page<MemberPasswordChangeHistoryRes.Item> page = memberPasswordChangeHistoryPort.search(filter, pageable);
        applyAuditorNames(page.getContent());
        return page;
    }

    @Transactional
    public MemberPasswordChangeHistoryRes.Id save(MemberPasswordChangeHistoryReq.Create create) {
        return memberPasswordChangeHistoryPort.save(create);
    }

    public MemberPasswordChangeHistoryRes.Item findById(Integer id) {
        MemberPasswordChangeHistoryRes.Item item = memberPasswordChangeHistoryPort.findById(id);
        applyAuditorNames(List.of(item));
        return item;
    }

    @Transactional
    public MemberPasswordChangeHistoryRes.Id saveById(Integer id, MemberPasswordChangeHistoryReq.Update update) {
        return memberPasswordChangeHistoryPort.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberPasswordChangeHistoryPort.deleteById(id);
    }

    public List<MemberPasswordChangeHistoryRes.Item> findAll(MemberPasswordChangeHistoryReq.Filter filter) {
        List<MemberPasswordChangeHistoryRes.Item> items = memberPasswordChangeHistoryPort.findAll(filter);
        applyAuditorNames(items);
        return items;
    }

    public List<MemberPasswordChangeHistoryRes.Item> findByIds(List<Integer> ids) {
        List<MemberPasswordChangeHistoryRes.Item> items = memberPasswordChangeHistoryPort.findByIds(ids);
        applyAuditorNames(items);
        return items;
    }

    public Page<MemberPasswordChangeHistoryRes.Name> searchName(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        return memberPasswordChangeHistoryPort.searchName(filter, pageable);
    }

    public List<MemberPasswordChangeHistoryRes.Name> findAllName(MemberPasswordChangeHistoryReq.Filter filter) {
        return memberPasswordChangeHistoryPort.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberPasswordChangeHistoryPort.deleteByIds(ids);
    }

    private void applyAuditorNames(List<MemberPasswordChangeHistoryRes.Item> items) {
        displayNameResolver.applyAuditorDisplayNames(
                items,
                MemberPasswordChangeHistoryRes.Item::getCreatedBy,
                MemberPasswordChangeHistoryRes.Item::setCreatedBy,
                MemberPasswordChangeHistoryRes.Item::getUpdatedBy,
                MemberPasswordChangeHistoryRes.Item::setUpdatedBy
        );
    }
}
