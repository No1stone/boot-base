package com.origemite.lib.model.member.persistence;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.BeanUtils;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.member.dto.MemberOutReq;
import com.origemite.lib.model.member.dto.MemberOutRes;
import com.origemite.lib.model.member.entity.MemberOut;
import com.origemite.lib.model.member.repository.MemberOutRepository;
import com.origemite.lib.model.member.spec.MemberOutSpecs;
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
public class MemberOutAdapter implements MemberOutPort {

    private final MemberOutRepository memberOutRepository;

    @Override
    public Page<MemberOutRes.Item> search(MemberOutReq.Filter filter, Pageable pageable) {
        Page<MemberOut> page = memberOutRepository.findAll(MemberOutSpecs.of(filter), pageable);
        return mapAll(page, MemberOutRes.Item.class);
    }

    @Override
    @Transactional
    public MemberOutRes.Id save(MemberOutReq.Create create) {
        MemberOut entity = map(create, MemberOut.class);
        entity = memberOutRepository.save(entity);
        return map(entity, MemberOutRes.Id.class);
    }

    @Override
    @Transactional
    public List<MemberOutRes.Id> saveAll(List<MemberOutReq.Create> createList) {
        if (createList == null || createList.isEmpty()) {
            return List.of();
        }
        List<MemberOut> saveList = createList.stream()
                .map(item -> map(item, MemberOut.class))
                .toList();
        return mapAll(memberOutRepository.saveAll(saveList), MemberOutRes.Id.class);
    }

    @Override
    public MemberOutRes.Item findById(Integer id) {
        MemberOut entity = memberOutRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        return map(entity, MemberOutRes.Item.class);
    }

    @Override
    @Transactional
    public MemberOutRes.Id saveById(Integer id, MemberOutReq.Update update) {
        MemberOut entity = memberOutRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.NOT_FOUND_RESOURCE));
        BeanUtils.map(update, entity);
        return map(entity, MemberOutRes.Id.class);
    }

    @Override
    @Transactional
    public void deleteById(Integer id) {
        MemberOut entity = memberOutRepository.findById(id)
                .orElseThrow(() -> new BizErrorException(ResponseType.UNDEFINED));
        entity.delete();
    }

    @Override
    public List<MemberOutRes.Item> findAll(MemberOutReq.Filter filter) {
        return search(filter, Pageable.unpaged()).getContent();
    }

    @Override
    public List<MemberOutRes.Item> findByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        MemberOutReq.Filter filter = new MemberOutReq.Filter();
        filter.setIds(ids);
        return mapAll(memberOutRepository.findAll(MemberOutSpecs.of(filter)), MemberOutRes.Item.class);
    }

    @Override
    public Page<MemberOutRes.Name> searchName(MemberOutReq.Filter filter, Pageable pageable) {
        Page<MemberOut> page = memberOutRepository.findAll(MemberOutSpecs.of(filter), pageable);
        return mapAll(page, MemberOutRes.Name.class);
    }

    @Override
    public List<MemberOutRes.Name> findAllName(MemberOutReq.Filter filter) {
        return searchName(filter, Pageable.unpaged()).getContent();
    }

    @Override
    @Transactional
    public void deleteByIds(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        MemberOutReq.Filter filter = new MemberOutReq.Filter();
        filter.setIds(ids);
        memberOutRepository.findAll(MemberOutSpecs.of(filter)).forEach(MemberOut::delete);
    }
}
