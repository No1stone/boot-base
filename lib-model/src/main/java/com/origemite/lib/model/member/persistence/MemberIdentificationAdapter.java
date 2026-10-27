package com.origemite.lib.model.member.persistence;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.member.dto.MemberIdentificationReq;
import com.origemite.lib.model.member.dto.MemberIdentificationRes;
import com.origemite.lib.model.member.entity.MemberIdentification;
import com.origemite.lib.model.member.repository.MemberIdentificationRepository;
import com.origemite.lib.model.member.spec.MemberIdentificationSpecs;
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
public class MemberIdentificationAdapter implements MemberIdentificationPort {

    private final MemberIdentificationRepository memberIdentificationRepository;

    @Override
    public Page<MemberIdentificationRes.Item> search(MemberIdentificationReq.Filter filter, Pageable pageable) {
        Page<MemberIdentification> page = memberIdentificationRepository.findAll(MemberIdentificationSpecs.of(filter), pageable);
        return mapAll(page, MemberIdentificationRes.Item.class);
    }

    @Override
    @Transactional
    public MemberIdentificationRes.Id save(MemberIdentificationReq.Create create) {
        MemberIdentification entity = map(create, MemberIdentification.class);
        entity = memberIdentificationRepository.save(entity);
        return map(entity, MemberIdentificationRes.Id.class);
    }

    @Override
    @Transactional
    public List<MemberIdentificationRes.Id> saveAll(List<MemberIdentificationReq.Create> createList) {
        if (createList == null || createList.isEmpty()) {
            return List.of();
        }
        List<MemberIdentification> saveList = createList.stream()
                .map(item -> map(item, MemberIdentification.class))
                .toList();
        return mapAll(memberIdentificationRepository.saveAll(saveList), MemberIdentificationRes.Id.class);
    }

    @Override
    public MemberIdentificationRes.Item findById(Integer id) {
        MemberIdentification entity = memberIdentificationRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberIdentificationRes.Item.class);
    }

    @Override
    @Transactional
    public MemberIdentificationRes.Id saveById(Integer id, MemberIdentificationReq.Update update) {
        MemberIdentification entity = memberIdentificationRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberIdentificationRes.Id.class);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        MemberIdentification entity = memberIdentificationRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    @Override
    public List<MemberIdentificationRes.Item> findAll(MemberIdentificationReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    @Override
    public List<MemberIdentificationRes.Item> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        MemberIdentificationReq.Filter filter = new MemberIdentificationReq.Filter();
        filter.setIds(ids);
        return mapAll(memberIdentificationRepository.findAll(MemberIdentificationSpecs.of(filter)), MemberIdentificationRes.Item.class);
    }

    @Override
    public Page<MemberIdentificationRes.Name> searchName(MemberIdentificationReq.Filter filter, Pageable pageable) {
        Page<MemberIdentification> page = memberIdentificationRepository.findAll(MemberIdentificationSpecs.of(filter), pageable);
        return mapAll(page, MemberIdentificationRes.Name.class);
    }

    @Override
    public List<MemberIdentificationRes.Name> findAllName(MemberIdentificationReq.Filter filter) {
        return searchName(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        MemberIdentificationReq.Filter filter = new MemberIdentificationReq.Filter();
        filter.setIds(ids);
        memberIdentificationRepository.findAll(MemberIdentificationSpecs.of(filter)).forEach(MemberIdentification::delete);
    }
}
