package com.origemite.lib.model.auth.service;

import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.common.web.SystemServiceModule;
import com.origemite.lib.model.auth.dto.AppMemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.AppMemberLoginHistoryRes;
import com.origemite.lib.model.auth.entity.AppMemberLoginHistory;
import com.origemite.lib.model.auth.repository.AppMemberLoginHistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.origemite.lib.common.util.ModelMapperUtil.map;
import static com.origemite.lib.common.util.ModelMapperUtil.mapAll;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AppMemberLoginHistoryService {

    private final AppMemberLoginHistoryRepository appMemberLoginHistoryRepository;

    public Page<AppMemberLoginHistoryRes.Item> search(AppMemberLoginHistoryReq.Filter filter, Pageable pageable) {
        AppMemberLoginHistory entity = map(filter, AppMemberLoginHistory.class);
        Page<AppMemberLoginHistory> page = appMemberLoginHistoryRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, AppMemberLoginHistoryRes.Item.class);
    }

    @Transactional
    public AppMemberLoginHistoryRes.Id save(AppMemberLoginHistoryReq.Create create) {
        AppMemberLoginHistory entity = map(create, AppMemberLoginHistory.class);
        entity = appMemberLoginHistoryRepository.save(entity);
        return map(entity, AppMemberLoginHistoryRes.Id.class);
    }

    public AppMemberLoginHistoryRes.Item findById(Integer id) {
        AppMemberLoginHistory entity = appMemberLoginHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        return map(entity, AppMemberLoginHistoryRes.Item.class);
    }

    @Transactional
    public AppMemberLoginHistoryRes.Id saveById(Integer id, AppMemberLoginHistoryReq.Update update) {
        AppMemberLoginHistory entity = appMemberLoginHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        BeanUtils.map(update, entity);
        return map(entity, AppMemberLoginHistoryRes.Id.class);
    }

    @Transactional
    public void deleteById(Integer id) {
        AppMemberLoginHistory entity = appMemberLoginHistoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        entity.delete();
    }

    public List<AppMemberLoginHistoryRes.Item> findAll(AppMemberLoginHistoryReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<AppMemberLoginHistoryRes.Item> findByIds(List<Integer> ids) {
        List<AppMemberLoginHistory> list = appMemberLoginHistoryRepository.findByIdIn(ids);
        return mapAll(list, AppMemberLoginHistoryRes.Item.class);
    }

    public Page<AppMemberLoginHistoryRes.Name> searchName(AppMemberLoginHistoryReq.Filter filter, Pageable pageable) {
        AppMemberLoginHistory entity = map(filter, AppMemberLoginHistory.class);
        Page<AppMemberLoginHistory> page = appMemberLoginHistoryRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, AppMemberLoginHistoryRes.Name.class);
    }

    public List<AppMemberLoginHistoryRes.Name> findAllName(AppMemberLoginHistoryReq.Filter filter) {
        AppMemberLoginHistory entity = map(filter, AppMemberLoginHistory.class);
        Page<AppMemberLoginHistory> page = appMemberLoginHistoryRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), AppMemberLoginHistoryRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<Integer> ids) {
        appMemberLoginHistoryRepository.findByIdIn(ids).forEach(AppMemberLoginHistory::delete);
    }
}