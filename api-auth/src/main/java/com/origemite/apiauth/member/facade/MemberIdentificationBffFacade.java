package com.origemite.apiauth.member.facade;

import com.origemite.lib.model.member.dto.MemberIdentificationReq;
import com.origemite.lib.model.member.dto.MemberIdentificationRes;
import com.origemite.lib.model.member.facade.MemberIdentificationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * HTTP BFF 진입. 도메인 진입({@link MemberIdentificationFacade})만 호출한다. Port는 보지 않는다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class MemberIdentificationBffFacade {

    private final MemberIdentificationFacade memberIdentificationFacade;

    public Page<MemberIdentificationRes.Item> search(MemberIdentificationReq.Filter filter, Pageable pageable) {
        return memberIdentificationFacade.search(filter, pageable);
    }

    public MemberIdentificationRes.Id save(MemberIdentificationReq.CreateForEncrypt create) {
        return memberIdentificationFacade.save(create);
    }

    public MemberIdentificationRes.Id save(MemberIdentificationReq.Create create) {
        return memberIdentificationFacade.save(create);
    }

    public MemberIdentificationRes.Item findById(Integer id) {
        return memberIdentificationFacade.findById(id);
    }

    public MemberIdentificationRes.Id saveById(Integer id, MemberIdentificationReq.Update update) {
        return memberIdentificationFacade.saveById(id, update);
    }

    public void deleteById(Integer id) {
        memberIdentificationFacade.deleteById(id);
    }

    public List<MemberIdentificationRes.Item> findAll(MemberIdentificationReq.Filter filter) {
        return memberIdentificationFacade.findAll(filter);
    }

    public List<MemberIdentificationRes.Item> findByIds(List<Integer> ids) {
        return memberIdentificationFacade.findByIds(ids);
    }

    public Page<MemberIdentificationRes.Name> searchName(MemberIdentificationReq.Filter filter, Pageable pageable) {
        return memberIdentificationFacade.searchName(filter, pageable);
    }

    public List<MemberIdentificationRes.Name> findAllName(MemberIdentificationReq.Filter filter) {
        return memberIdentificationFacade.findAllName(filter);
    }

    public void deleteByIds(List<Integer> ids) {
        memberIdentificationFacade.deleteByIds(ids);
    }
}
