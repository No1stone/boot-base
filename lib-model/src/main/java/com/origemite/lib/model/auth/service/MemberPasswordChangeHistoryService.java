package com.origemite.lib.model.auth.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.origemite.lib.model.auth.entity.MemberPasswordChangeHistory;
import com.origemite.lib.model.auth.repository.MemberPasswordChangeHistoryRepository;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryRes;
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
public class MemberPasswordChangeHistoryService {

    private final MemberPasswordChangeHistoryRepository memberPasswordChangeHistoryRepository;

    public Page<MemberPasswordChangeHistoryRes.Item> search(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        MemberPasswordChangeHistory entity = map(filter, MemberPasswordChangeHistory.class);
        Page<MemberPasswordChangeHistory> page = memberPasswordChangeHistoryRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberPasswordChangeHistoryRes.Item.class);
    }

    @Transactional
    public MemberPasswordChangeHistoryRes.Id save(MemberPasswordChangeHistoryReq.Create create) {
        MemberPasswordChangeHistory entity = map(create, MemberPasswordChangeHistory.class);
        entity = memberPasswordChangeHistoryRepository.save(entity);
        return map(entity, MemberPasswordChangeHistoryRes.Id.class);
    }

    public MemberPasswordChangeHistoryRes.Item findById(Integer id) {
        MemberPasswordChangeHistory entity = memberPasswordChangeHistoryRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberPasswordChangeHistoryRes.Item.class);
    }

    @Transactional
    public MemberPasswordChangeHistoryRes.Id saveById(Integer id, MemberPasswordChangeHistoryReq.Update update) {
        MemberPasswordChangeHistory entity = memberPasswordChangeHistoryRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberPasswordChangeHistoryRes.Id.class);
    }

    @Transactional
    public void deleteById(Integer id) {
        MemberPasswordChangeHistory entity = memberPasswordChangeHistoryRepository.findById(id).orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    public List<MemberPasswordChangeHistoryRes.Item> findAll(MemberPasswordChangeHistoryReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<MemberPasswordChangeHistoryRes.Item> findByIds(List<Integer> ids) {
        List<MemberPasswordChangeHistory> list = memberPasswordChangeHistoryRepository.findByIdIn(ids);
        return mapAll(list, MemberPasswordChangeHistoryRes.Item.class);
    }

    public Page<MemberPasswordChangeHistoryRes.Name> searchName(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        MemberPasswordChangeHistory entity = map(filter, MemberPasswordChangeHistory.class);
        Page<MemberPasswordChangeHistory> page = memberPasswordChangeHistoryRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, MemberPasswordChangeHistoryRes.Name.class);
    }

    public List<MemberPasswordChangeHistoryRes.Name> findAllName(MemberPasswordChangeHistoryReq.Filter filter) {
        MemberPasswordChangeHistory entity = map(filter, MemberPasswordChangeHistory.class);
        Page<MemberPasswordChangeHistory> page = memberPasswordChangeHistoryRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), MemberPasswordChangeHistoryRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        memberPasswordChangeHistoryRepository.findByIdIn(ids).forEach(MemberPasswordChangeHistory::delete);
    }
}