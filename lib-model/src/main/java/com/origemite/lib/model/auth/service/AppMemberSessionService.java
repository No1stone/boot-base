package com.origemite.lib.model.auth.service;

import com.origemite.lib.model.auth.dto.AppMemberSessionReq;
import com.origemite.lib.model.auth.dto.AppMemberSessionRes;
import com.origemite.lib.model.auth.entity.AppMemberSession;
import com.origemite.lib.model.auth.repository.AppMemberSessionRepository;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.common.web.SystemServiceModule;
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
public class AppMemberSessionService {

    private final AppMemberSessionRepository appMemberSessionRepository;

    public Page<AppMemberSessionRes.Item> search(AppMemberSessionReq.Filter filter, Pageable pageable) {
        AppMemberSession entity = map(filter, AppMemberSession.class);
        Page<AppMemberSession> page = appMemberSessionRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, AppMemberSessionRes.Item.class);
    }

    @Transactional
    public AppMemberSessionRes.Id save(AppMemberSessionReq.Create create) {
        AppMemberSession entity = map(create, AppMemberSession.class);
        entity = appMemberSessionRepository.save(entity);
        return map(entity, AppMemberSessionRes.Id.class);
    }

    public AppMemberSessionRes.Item findById(String id) {
        AppMemberSession entity = appMemberSessionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        return map(entity, AppMemberSessionRes.Item.class);
    }

    @Transactional
    public AppMemberSessionRes.Id saveById(String id, AppMemberSessionReq.Update update) {
        AppMemberSession entity = appMemberSessionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        BeanUtils.map(update, entity);
        return map(entity, AppMemberSessionRes.Id.class);
    }

    @Transactional
    public void deleteById(String id) {
        AppMemberSession entity = appMemberSessionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException());
        entity.delete();
    }

    public List<AppMemberSessionRes.Item> findAll(AppMemberSessionReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    public List<AppMemberSessionRes.Item> findByIds(List<String> ids) {
        List<AppMemberSession> list = appMemberSessionRepository.findByIdIn(ids);
        return mapAll(list, AppMemberSessionRes.Item.class);
    }

    public Page<AppMemberSessionRes.Name> searchName(AppMemberSessionReq.Filter filter, Pageable pageable) {
        AppMemberSession entity = map(filter, AppMemberSession.class);
        Page<AppMemberSession> page = appMemberSessionRepository.findAll(Example.of(entity), pageable);
        return mapAll(page, AppMemberSessionRes.Name.class);
    }

    public List<AppMemberSessionRes.Name> findAllName(AppMemberSessionReq.Filter filter) {
        AppMemberSession entity = map(filter, AppMemberSession.class);
        Page<AppMemberSession> page = appMemberSessionRepository.findAll(Example.of(entity), Pageable.unpaged());
        return mapAll(page.stream().toList(), AppMemberSessionRes.Name.class);
    }

    @Transactional
    public void deleteByIds(List<String> ids) {
        appMemberSessionRepository.findByIdIn(ids).forEach(AppMemberSession::delete);
    }
    public AppMemberSessionRes.Single loginByRefreshToken(String refreshToken) {
        AppMemberSessionRes.Single administratorSession = map(appMemberSessionRepository.findActiveByRefreshToken(refreshToken), AppMemberSessionRes.Single.class);
        return administratorSession;
    };


}