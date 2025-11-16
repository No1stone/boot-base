package com.origemite.lib.model.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.origemite.lib.model.auth.entity.MemberIdentification;
import com.origemite.lib.model.auth.repository.MemberIdentificationRepository;
import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
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
public class MemberIdentificationService {

    private final MemberIdentificationRepository memberIdentificationRepository;

    public Page<MemberIdentificationRes.Item> search(MemberIdentificationReq.Filter filter, Pageable pageable) {
        MemberIdentification entity = map(filter, MemberIdentification.class);
        Page<MemberIdentification> page = memberIdentificationRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberIdentificationRes.Item.class);
    }

    @Transactional
    public MemberIdentificationRes.Id save(MemberIdentificationReq.Create create) {
        MemberIdentification entity = map(create, MemberIdentification.class);
        entity = memberIdentificationRepository.save(entity);
        return map(entity, MemberIdentificationRes.Id.class);
    }

    public MemberIdentificationRes.Item findById(Integer id) {
        MemberIdentification entity = memberIdentificationRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberIdentificationRes.Item.class);
    }

    @Transactional
    public MemberIdentificationRes.Id saveById(Integer id, MemberIdentificationReq.Update update) {
        MemberIdentification entity = memberIdentificationRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberIdentificationRes.Id.class);
    }

    @Transactional
    public void deleteById(Integer id) {
        MemberIdentification entity = memberIdentificationRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    public List<MemberIdentificationRes.Item> findAll(MemberIdentificationReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<MemberIdentificationRes.Item> findByIds(List<Integer> ids) {
        List<MemberIdentification> list = memberIdentificationRepository.findByIdIn(ids);
        return mapAll(list, MemberIdentificationRes.Item.class);
    }

    public Page<MemberIdentificationRes.Name> searchName(MemberIdentificationReq.Filter filter, Pageable pageable) {
        MemberIdentification entity = map(filter, MemberIdentification.class);
        Page<MemberIdentification> page = memberIdentificationRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberIdentificationRes.Name.class);
    }

    public List<MemberIdentificationRes.Name> findAllName(MemberIdentificationReq.Filter filter) {
        MemberIdentification entity = map(filter, MemberIdentification.class);
        Page<MemberIdentification> page = memberIdentificationRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), MemberIdentificationRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberIdentificationRepository.findByIdIn(ids).forEach(MemberIdentification::delete);
    }
}