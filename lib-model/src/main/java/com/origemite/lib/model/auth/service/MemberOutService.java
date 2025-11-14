package com.origemite.lib.model.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.origemite.lib.model.auth.entity.MemberOut;
import com.origemite.lib.model.auth.repository.MemberOutRepository;
import com.origemite.lib.model.auth.dto.MemberOutReq;
import com.origemite.lib.model.auth.dto.MemberOutRes;
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
public class MemberOutService {

    private final MemberOutRepository memberOutRepository;

    public Page<MemberOutRes.Item> search(MemberOutReq.Filter filter, Pageable pageable) {
        MemberOut entity = map(filter, MemberOut.class);
        Page<MemberOut> page = memberOutRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberOutRes.Item.class);
    }

    @Transactional
    public MemberOutRes.Id save(MemberOutReq.Create create) {
        MemberOut entity = map(create, MemberOut.class);
        entity = memberOutRepository.save(entity);
        return map(entity, MemberOutRes.Id.class);
    }

    public MemberOutRes.Item findById(Integer id) {
        MemberOut entity = memberOutRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberOutRes.Item.class);
    }

    @Transactional
    public MemberOutRes.Id saveById(Integer id, MemberOutReq.Update update) {
        MemberOut entity = memberOutRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberOutRes.Id.class);
    }

    @Transactional
    public void deleteById(Integer id) {
        MemberOut entity = memberOutRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    public List<MemberOutRes.Item> findAll(MemberOutReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<MemberOutRes.Item> findByIds(List<Integer> ids) {
        List<MemberOut> list = memberOutRepository.findByIdIn(ids);
        return mapAll(list, MemberOutRes.Item.class);
    }

    public Page<MemberOutRes.Name> searchName(MemberOutReq.Filter filter, Pageable pageable) {
        MemberOut entity = map(filter, MemberOut.class);
        Page<MemberOut> page = memberOutRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberOutRes.Name.class);
    }

    public List<MemberOutRes.Name> findAllName(MemberOutReq.Filter filter) {
        MemberOut entity = map(filter, MemberOut.class);
        Page<MemberOut> page = memberOutRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), MemberOutRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberOutRepository.findByIdIn(ids).forEach(MemberOut::delete);
    }
}