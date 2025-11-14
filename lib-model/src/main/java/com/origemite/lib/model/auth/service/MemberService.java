package com.origemite.lib.model.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.origemite.lib.model.auth.entity.Member;
import com.origemite.lib.model.auth.repository.MemberRepository;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import java.util.*;

import static com.origemite.lib.common.util.ModelMapperUtil.map;
import static com.origemite.lib.common.util.ModelMapperUtil.mapAll;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    public Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable) {
        Member entity = map(filter, Member.class);
        Page<Member> page = memberRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberRes.Item.class);
    }

    @Transactional
    public MemberRes.Id save(MemberReq.Create create) {
        Member entity = map(create, Member.class);
        entity = memberRepository.save(entity);
        return map(entity, MemberRes.Id.class);
    }

    public MemberRes.Item findById(String id) {
        Member entity = memberRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberRes.Item.class);
    }

    @Transactional
    public MemberRes.Id saveById(String id, MemberReq.Update update) {
        Member entity = memberRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberRes.Id.class);
    }

    @Transactional
    public void deleteById(String id) {
        Member entity = memberRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    public List<MemberRes.Item> findAll(MemberReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<MemberRes.Item> findByIds(List<String> ids) {
        List<Member> list = memberRepository.findByIdIn(ids);
        return mapAll(list, MemberRes.Item.class);
    }

    public Page<MemberRes.Name> searchName(MemberReq.Filter filter, Pageable pageable) {
        Member entity = map(filter, Member.class);
        Page<Member> page = memberRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberRes.Name.class);
    }

    public List<MemberRes.Name> findAllName(MemberReq.Filter filter) {
        Member entity = map(filter, Member.class);
        Page<Member> page = memberRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), MemberRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<String> ids) {
        memberRepository.findByIdIn(ids).forEach(Member::delete);
    }
}