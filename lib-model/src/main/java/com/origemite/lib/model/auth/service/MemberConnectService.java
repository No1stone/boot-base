package com.origemite.lib.model.auth.service;

import com.origemite.lib.model.auth.dto.MemberConnectRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.origemite.lib.model.auth.entity.MemberConnect;
import com.origemite.lib.model.auth.repository.MemberConnectRepository;
import com.origemite.lib.model.auth.dto.MemberConnectReq;
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
public class MemberConnectService {

    private final MemberConnectRepository memberConnectRepository;

    public Page<MemberConnectRes.Item> search(MemberConnectReq.Filter filter, Pageable pageable) {
        MemberConnect entity = map(filter, MemberConnect.class);
        Page<MemberConnect> page = memberConnectRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberConnectRes.Item.class);
    }

    @Transactional
    public MemberConnectRes.Id save(MemberConnectReq.Create create) {
        MemberConnect entity = map(create, MemberConnect.class);
        entity = memberConnectRepository.save(entity);
        return map(entity, MemberConnectRes.Id.class);
    }

    public MemberConnectRes.Item findById(Integer id) {
        MemberConnect entity = memberConnectRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberConnectRes.Item.class);
    }

    @Transactional
    public MemberConnectRes.Id saveById(Integer id, MemberConnectReq.Update update) {
        MemberConnect entity = memberConnectRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberConnectRes.Id.class);
    }

    @Transactional
    public void deleteById(Integer id) {
        MemberConnect entity = memberConnectRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    public List<MemberConnectRes.Item> findAll(MemberConnectReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<MemberConnectRes.Item> findByIds(List<Integer> ids) {
        List<MemberConnect> list = memberConnectRepository.findByIdIn(ids);
        return mapAll(list, MemberConnectRes.Item.class);
    }

    public Page<MemberConnectRes.Name> searchName(MemberConnectReq.Filter filter, Pageable pageable) {
        MemberConnect entity = map(filter, MemberConnect.class);
        Page<MemberConnect> page = memberConnectRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberConnectRes.Name.class);
    }

    public List<MemberConnectRes.Name> findAllName(MemberConnectReq.Filter filter) {
        MemberConnect entity = map(filter, MemberConnect.class);
        Page<MemberConnect> page = memberConnectRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), MemberConnectRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberConnectRepository.findByIdIn(ids).forEach(MemberConnect::delete);
    }
}