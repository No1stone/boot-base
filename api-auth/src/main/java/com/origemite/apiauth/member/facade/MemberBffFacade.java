package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import com.origemite.lib.model.member.facade.MemberFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HTTP BFF 진입. 도메인 진입({@link MemberFacade})만 호출한다. Port는 보지 않는다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberBffFacade {

    private final MemberFacade memberFacade;

    public Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable) {
        return memberFacade.search(filter, pageable);
    }

    public MemberRes.Id save(MemberReq.Create create) {
        return memberFacade.save(create);
    }

    public List<MemberRes.Id> saveAll(List<MemberReq.Create> createList) {
        return memberFacade.saveAll(createList);
    }

    public MemberRes.Item findById(String id) {
        return memberFacade.findById(id);
    }

    public MemberRes.Id saveById(String id, MemberReq.Update update) {
        return memberFacade.saveById(id, update);
    }

    public void deleteById(String id) {
        memberFacade.deleteById(id);
    }

    public List<MemberRes.Item> findAll(MemberReq.Filter filter) {
        return memberFacade.findAll(filter);
    }

    public List<MemberRes.Item> findByIds(List<String> ids) {
        return memberFacade.findByIds(ids);
    }

    public Page<MemberRes.Name> searchName(MemberReq.Filter filter, Pageable pageable) {
        return memberFacade.searchName(filter, pageable);
    }

    public List<MemberRes.Name> findAllName(MemberReq.Filter filter) {
        return memberFacade.findAllName(filter);
    }

    public void deleteByIds(List<String> ids) {
        memberFacade.deleteByIds(ids);
    }
}
