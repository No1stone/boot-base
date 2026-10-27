package com.origemite.lib.model.auth.persistence;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryRes;
import com.origemite.lib.model.auth.entity.MemberLoginHistory;
import com.origemite.lib.model.auth.repository.MemberLoginHistoryRepository;
import com.origemite.lib.model.auth.spec.MemberLoginHistorySpecs;
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
public class MemberLoginHistoryAdapter implements MemberLoginHistoryPort {

    private final MemberLoginHistoryRepository memberLoginHistoryRepository;

    @Override
    public Page<MemberLoginHistoryRes.Item> search(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        Page<MemberLoginHistory> page = memberLoginHistoryRepository.findAll(MemberLoginHistorySpecs.of(filter), pageable);
        return mapAll(page, MemberLoginHistoryRes.Item.class);
    }

    @Override
    @Transactional
    public MemberLoginHistoryRes.Id save(MemberLoginHistoryReq.Create create) {
        MemberLoginHistory entity = map(create, MemberLoginHistory.class);
        entity = memberLoginHistoryRepository.save(entity);
        return map(entity, MemberLoginHistoryRes.Id.class);
    }

    @Override
    @Transactional
    public List<MemberLoginHistoryRes.Id> saveAll(List<MemberLoginHistoryReq.Create> createList) {
        if (createList == null || createList.isEmpty()) {
            return List.of();
        }
        List<MemberLoginHistory> saveList = createList.stream()
                .map(item -> map(item, MemberLoginHistory.class))
                .toList();
        return mapAll(memberLoginHistoryRepository.saveAll(saveList), MemberLoginHistoryRes.Id.class);
    }

    @Override
    public MemberLoginHistoryRes.Item findById(Integer id) {
        MemberLoginHistory entity = memberLoginHistoryRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberLoginHistoryRes.Item.class);
    }

    @Override
    @Transactional
    public MemberLoginHistoryRes.Id saveById(Integer id, MemberLoginHistoryReq.Update update) {
        MemberLoginHistory entity = memberLoginHistoryRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberLoginHistoryRes.Id.class);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        MemberLoginHistory entity = memberLoginHistoryRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    @Override
    public List<MemberLoginHistoryRes.Item> findAll(MemberLoginHistoryReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    @Override
    public List<MemberLoginHistoryRes.Item> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        MemberLoginHistoryReq.Filter filter = new MemberLoginHistoryReq.Filter();
        filter.setIds(ids);
        return mapAll(memberLoginHistoryRepository.findAll(MemberLoginHistorySpecs.of(filter)), MemberLoginHistoryRes.Item.class);
    }

    @Override
    public Page<MemberLoginHistoryRes.Name> searchName(MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        Page<MemberLoginHistory> page = memberLoginHistoryRepository.findAll(MemberLoginHistorySpecs.of(filter), pageable);
        return mapAll(page, MemberLoginHistoryRes.Name.class);
    }

    @Override
    public List<MemberLoginHistoryRes.Name> findAllName(MemberLoginHistoryReq.Filter filter) {
        return searchName(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        MemberLoginHistoryReq.Filter filter = new MemberLoginHistoryReq.Filter();
        filter.setIds(ids);
        memberLoginHistoryRepository.findAll(MemberLoginHistorySpecs.of(filter)).forEach(MemberLoginHistory::delete);
    }
}
