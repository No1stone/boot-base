package com.origemite.lib.model.auth.service;

import com.origemite.lib.model.auth.dto.MemberLoginHistoryRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.origemite.lib.model.auth.entity.MemberLoginHistory;
import com.origemite.lib.model.auth.repository.MemberLoginHistoryRepository;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
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
public class MemberLoginHistoryService {

    private final MemberLoginHistoryRepository memberLoginHistoryRepository;

    public Page<MemberLoginHistoryRes.Item> search(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        MemberLoginHistory entity = map(filter, MemberLoginHistory.class);
        Page<MemberLoginHistory> page = memberLoginHistoryRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberLoginHistoryRes.Item.class);
    }

    @Transactional
    public MemberLoginHistoryRes.Id save(MemberLoginHistoryReq.Create create) {
        MemberLoginHistory entity = map(create, MemberLoginHistory.class);
        entity = memberLoginHistoryRepository.save(entity);
        return map(entity, MemberLoginHistoryRes.Id.class);
    }

    public MemberLoginHistoryRes.Item findById(Integer id) {
        MemberLoginHistory entity = memberLoginHistoryRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberLoginHistoryRes.Item.class);
    }

    @Transactional
    public MemberLoginHistoryRes.Id saveById(Integer id, MemberLoginHistoryReq.Update update) {
        MemberLoginHistory entity = memberLoginHistoryRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberLoginHistoryRes.Id.class);
    }

    @Transactional
    public void deleteById(Integer id) {
        MemberLoginHistory entity = memberLoginHistoryRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    public List<MemberLoginHistoryRes.Item> findAll(MemberLoginHistoryReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<MemberLoginHistoryRes.Item> findByIds(List<Integer> ids) {
        List<MemberLoginHistory> list = memberLoginHistoryRepository.findByIdIn(ids);
        return mapAll(list, MemberLoginHistoryRes.Item.class);
    }

    public Page<MemberLoginHistoryRes.Name> searchName(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        MemberLoginHistory entity = map(filter, MemberLoginHistory.class);
        Page<MemberLoginHistory> page = memberLoginHistoryRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberLoginHistoryRes.Name.class);
    }

    public List<MemberLoginHistoryRes.Name> findAllName(MemberLoginHistoryReq.Filter filter) {
        MemberLoginHistory entity = map(filter, MemberLoginHistory.class);
        Page<MemberLoginHistory> page = memberLoginHistoryRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), MemberLoginHistoryRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberLoginHistoryRepository.findByIdIn(ids).forEach(MemberLoginHistory::delete);
    }
}