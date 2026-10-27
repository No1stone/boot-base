package com.origemite.lib.model.auth.persistence;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.dto.MemberConnectReq;
import com.origemite.lib.model.auth.dto.MemberConnectRes;
import com.origemite.lib.model.auth.entity.MemberConnect;
import com.origemite.lib.model.auth.repository.MemberConnectRepository;
import com.origemite.lib.model.auth.spec.MemberConnectSpecs;
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
public class MemberConnectAdapter implements MemberConnectPort {

    private final MemberConnectRepository memberConnectRepository;

    @Override
    public Page<MemberConnectRes.Item> search(MemberConnectReq.Filter filter, Pageable pageable) {
        Page<MemberConnect> page = memberConnectRepository.findAll(MemberConnectSpecs.of(filter), pageable);
        return mapAll(page, MemberConnectRes.Item.class);
    }

    @Override
    @Transactional
    public MemberConnectRes.Id save(MemberConnectReq.Create create) {
        MemberConnect entity = map(create, MemberConnect.class);
        entity = memberConnectRepository.save(entity);
        return map(entity, MemberConnectRes.Id.class);
    }

    @Override
    @Transactional
    public List<MemberConnectRes.Id> saveAll(List<MemberConnectReq.Create> createList) {
        if (createList == null || createList.isEmpty()) {
            return List.of();
        }
        List<MemberConnect> saveList = createList.stream()
                .map(item -> map(item, MemberConnect.class))
                .toList();
        return mapAll(memberConnectRepository.saveAll(saveList), MemberConnectRes.Id.class);
    }

    @Override
    public MemberConnectRes.Item findById(Integer id) {
        MemberConnect entity = memberConnectRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberConnectRes.Item.class);
    }

    @Override
    @Transactional
    public MemberConnectRes.Id saveById(Integer id, MemberConnectReq.Update update) {
        MemberConnect entity = memberConnectRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberConnectRes.Id.class);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        MemberConnect entity = memberConnectRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    @Override
    public List<MemberConnectRes.Item> findAll(MemberConnectReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    @Override
    public List<MemberConnectRes.Item> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        MemberConnectReq.Filter filter = new MemberConnectReq.Filter();
        filter.setIds(ids);
        return mapAll(memberConnectRepository.findAll(MemberConnectSpecs.of(filter)), MemberConnectRes.Item.class);
    }

    @Override
    public Page<MemberConnectRes.Name> searchName(MemberConnectReq.Filter filter, Pageable pageable) {
        Page<MemberConnect> page = memberConnectRepository.findAll(MemberConnectSpecs.of(filter), pageable);
        return mapAll(page, MemberConnectRes.Name.class);
    }

    @Override
    public List<MemberConnectRes.Name> findAllName(MemberConnectReq.Filter filter) {
        return searchName(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        MemberConnectReq.Filter filter = new MemberConnectReq.Filter();
        filter.setIds(ids);
        memberConnectRepository.findAll(MemberConnectSpecs.of(filter)).forEach(MemberConnect::delete);
    }
}
