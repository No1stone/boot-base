package com.origemite.lib.model.auth.persistence;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryRes;
import com.origemite.lib.model.auth.entity.MemberPasswordChangeHistory;
import com.origemite.lib.model.auth.repository.MemberPasswordChangeHistoryRepository;
import com.origemite.lib.model.auth.spec.MemberPasswordChangeHistorySpecs;
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
public class MemberPasswordChangeHistoryAdapter implements MemberPasswordChangeHistoryPort {

    private final MemberPasswordChangeHistoryRepository memberPasswordChangeHistoryRepository;

    @Override
    public Page<MemberPasswordChangeHistoryRes.Item> search(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        Page<MemberPasswordChangeHistory> page = memberPasswordChangeHistoryRepository.findAll(MemberPasswordChangeHistorySpecs.of(filter), pageable);
        return mapAll(page, MemberPasswordChangeHistoryRes.Item.class);
    }

    @Override
    @Transactional
    public MemberPasswordChangeHistoryRes.Id save(MemberPasswordChangeHistoryReq.Create create) {
        MemberPasswordChangeHistory entity = map(create, MemberPasswordChangeHistory.class);
        entity = memberPasswordChangeHistoryRepository.save(entity);
        return map(entity, MemberPasswordChangeHistoryRes.Id.class);
    }

    @Override
    @Transactional
    public List<MemberPasswordChangeHistoryRes.Id> saveAll(List<MemberPasswordChangeHistoryReq.Create> createList) {
        if (createList == null || createList.isEmpty()) {
            return List.of();
        }
        List<MemberPasswordChangeHistory> saveList = createList.stream()
                .map(item -> map(item, MemberPasswordChangeHistory.class))
                .toList();
        return mapAll(memberPasswordChangeHistoryRepository.saveAll(saveList), MemberPasswordChangeHistoryRes.Id.class);
    }

    @Override
    public MemberPasswordChangeHistoryRes.Item findById(Integer id) {
        MemberPasswordChangeHistory entity = memberPasswordChangeHistoryRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberPasswordChangeHistoryRes.Item.class);
    }

    @Override
    @Transactional
    public MemberPasswordChangeHistoryRes.Id saveById(Integer id, MemberPasswordChangeHistoryReq.Update update) {
        MemberPasswordChangeHistory entity = memberPasswordChangeHistoryRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberPasswordChangeHistoryRes.Id.class);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        MemberPasswordChangeHistory entity = memberPasswordChangeHistoryRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    @Override
    public List<MemberPasswordChangeHistoryRes.Item> findAll(MemberPasswordChangeHistoryReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    @Override
    public List<MemberPasswordChangeHistoryRes.Item> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        MemberPasswordChangeHistoryReq.Filter filter = new MemberPasswordChangeHistoryReq.Filter();
        filter.setIds(ids);
        return mapAll(memberPasswordChangeHistoryRepository.findAll(MemberPasswordChangeHistorySpecs.of(filter)), MemberPasswordChangeHistoryRes.Item.class);
    }

    @Override
    public Page<MemberPasswordChangeHistoryRes.Name> searchName(MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        Page<MemberPasswordChangeHistory> page = memberPasswordChangeHistoryRepository.findAll(MemberPasswordChangeHistorySpecs.of(filter), pageable);
        return mapAll(page, MemberPasswordChangeHistoryRes.Name.class);
    }

    @Override
    public List<MemberPasswordChangeHistoryRes.Name> findAllName(MemberPasswordChangeHistoryReq.Filter filter) {
        return searchName(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        MemberPasswordChangeHistoryReq.Filter filter = new MemberPasswordChangeHistoryReq.Filter();
        filter.setIds(ids);
        memberPasswordChangeHistoryRepository.findAll(MemberPasswordChangeHistorySpecs.of(filter)).forEach(MemberPasswordChangeHistory::delete);
    }
}
