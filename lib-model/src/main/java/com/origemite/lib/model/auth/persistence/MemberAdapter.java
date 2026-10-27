package com.origemite.lib.model.auth.persistence;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.model.auth.entity.Member;
import com.origemite.lib.model.auth.repository.MemberRepository;
import com.origemite.lib.model.auth.spec.MemberSpecs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.origemite.lib.common.util.ModelMapperUtil.map;
import static com.origemite.lib.common.util.ModelMapperUtil.mapAll;

@Component
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberAdapter implements MemberPort {

    private final MemberRepository memberRepository;

    @Override
    public Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable) {
        Page<Member> page = memberRepository.findAll(MemberSpecs.of(filter), pageable);
        return mapAll(page, MemberRes.Item.class);
    }

    @Override
    @Transactional
    public MemberRes.Id save(MemberReq.Create create) {
        Member entity = map(create, Member.class);
        entity = memberRepository.save(entity);
        return map(entity, MemberRes.Id.class);
    }

    @Override
    @Transactional
    public List<MemberRes.Id> saveAll(List<MemberReq.Create> createList) {
        if (createList == null || createList.isEmpty()) {
            return List.of();
        }
        List<Member> saveList = createList.stream()
                .map(item -> map(item, Member.class))
                .toList();
        return mapAll(memberRepository.saveAll(saveList), MemberRes.Id.class);
    }

    @Override
    public MemberRes.Item findById(String id) {
        Member entity = memberRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberRes.Item.class);
    }

    @Override
    @Transactional
    public MemberRes.Id saveById(String id, MemberReq.Update update) {
        Member entity = memberRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberRes.Id.class);
    }

    @Override
    @Transactional
    public void deleteById(String id) {
        Member entity = memberRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    @Override
    public List<MemberRes.Item> findAll(MemberReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    @Override
    public List<MemberRes.Item> findByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        MemberReq.Filter filter = new MemberReq.Filter();
        filter.setIds(ids);
        return mapAll(memberRepository.findAll(MemberSpecs.of(filter)), MemberRes.Item.class);
    }

    @Override
    public Page<MemberRes.Name> searchName(MemberReq.Filter filter, Pageable pageable) {
        Page<Member> page = memberRepository.findAll(MemberSpecs.of(filter), pageable);
        return mapAll(page, MemberRes.Name.class);
    }

    @Override
    public List<MemberRes.Name> findAllName(MemberReq.Filter filter) {
        return searchName(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public void deleteByIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        MemberReq.Filter filter = new MemberReq.Filter();
        filter.setIds(ids);
        memberRepository.findAll(MemberSpecs.of(filter)).forEach(Member::delete);
    }
}
