package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.member.support.display.DisplayNameResolver;
import com.origemite.lib.model.member.dto.MemberOutReq;
import com.origemite.lib.model.member.dto.MemberOutRes;
import com.origemite.lib.model.member.persistence.MemberOutPort;
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

    private final MemberOutPort memberOutPort;
    private final DisplayNameResolver displayNameResolver;

    public Page<MemberOutRes.Item> search(MemberOutReq.Filter filter, Pageable pageable) {
        Page<MemberOutRes.Item> page = memberOutPort.search(filter, pageable);
        applyAuditorNames(page.getContent());
        return page;
    }

    @Transactional
    public MemberOutRes.Id save(MemberOutReq.Create create) {
        return memberOutPort.save(create);
    }

    public MemberOutRes.Item findById(Integer id) {
        MemberOutRes.Item item = memberOutPort.findById(id);
        applyAuditorNames(List.of(item));
        return item;
    }

    @Transactional
    public MemberOutRes.Id saveById(Integer id, MemberOutReq.Update update) {
        return memberOutPort.saveById(id, update);
    }

    @Transactional
    public void deleteById(Integer id) {
        memberOutPort.deleteById(id);
    }

    public List<MemberOutRes.Item> findAll(MemberOutReq.Filter filter) {
        List<MemberOutRes.Item> items = memberOutPort.findAll(filter);
        applyAuditorNames(items);
        return items;
    }

    public List<MemberOutRes.Item> findByIds(List<Integer> ids) {
        List<MemberOutRes.Item> items = memberOutPort.findByIds(ids);
        applyAuditorNames(items);
        return items;
    }

    public Page<MemberOutRes.Name> searchName(MemberOutReq.Filter filter, Pageable pageable) {
        return memberOutPort.searchName(filter, pageable);
    }

    public List<MemberOutRes.Name> findAllName(MemberOutReq.Filter filter) {
        return memberOutPort.findAllName(filter);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberOutPort.deleteByIds(ids);
    }

    private void applyAuditorNames(List<MemberOutRes.Item> items) {
        displayNameResolver.applyAuditorDisplayNames(
                items,
                MemberOutRes.Item::getCreatedBy,
                MemberOutRes.Item::setCreatedBy,
                MemberOutRes.Item::getUpdatedBy,
                MemberOutRes.Item::setUpdatedBy
        );
    }
}
