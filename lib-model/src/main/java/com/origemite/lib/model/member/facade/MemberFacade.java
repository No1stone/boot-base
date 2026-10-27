package com.origemite.lib.model.member.facade;

import com.origemite.lib.common.util.EncUtils;
import com.origemite.lib.common.util.Salt;
import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import com.origemite.lib.model.member.persistence.MemberPort;
import com.origemite.lib.model.member.support.display.DisplayNameResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 멤버 도메인 진입. 조회 캐시는 {@code @Cacheable} → lib-cache {@code CachePort}(Redis).
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {

    public static final String CACHE_NAME = "member";

    private final MemberPort memberPort;
    private final DisplayNameResolver displayNameResolver;

    public Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable) {
        Page<MemberRes.Item> page = memberPort.search(filter, pageable);
        applyAuditorNames(page.getContent());
        return page;
    }

    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME, key = "#result.id")
    public MemberRes.Id save(MemberReq.Create create) {
        String salt = Salt.generate();
        create.setLoginPasswordSaltKey(salt);
        create.setLoginPassword(EncUtils.encSHA512(create.getLoginPassword(), salt));
        return memberPort.save(create);
    }

    @Cacheable(cacheNames = CACHE_NAME, key = "#id")
    public MemberRes.Item findById(String id) {
        MemberRes.Item item = memberPort.findById(id);
        stripSecrets(item);
        applyAuditorNames(List.of(item));
        return item;
    }

    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME, key = "#id")
    public MemberRes.Id saveById(String id, MemberReq.Update update) {
        return memberPort.saveById(id, update);
    }

    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME, key = "#id")
    public void deleteById(String id) {
        memberPort.deleteById(id);
    }

    public List<MemberRes.Item> findAll(MemberReq.Filter filter) {
        List<MemberRes.Item> items = memberPort.findAll(filter);
        applyAuditorNames(items);
        return items;
    }

    public List<MemberRes.Item> findByIds(List<String> ids) {
        List<MemberRes.Item> items = memberPort.findByIds(ids);
        applyAuditorNames(items);
        return items;
    }

    public Page<MemberRes.Name> searchName(MemberReq.Filter filter, Pageable pageable) {
        return memberPort.searchName(filter, pageable);
    }

    public List<MemberRes.Name> findAllName(MemberReq.Filter filter) {
        return memberPort.findAllName(filter);
    }

    @Transactional
    @CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
    public void deleteByIds(List<String> ids) {
        memberPort.deleteByIds(ids);
    }

    @Transactional
    public void login() {
    }

    @Transactional
    public void refreshToken() {
    }

    @Transactional
    public void create() {
    }

    @Transactional
    public void update() {
    }

    private static void stripSecrets(MemberRes.Item item) {
        if (item != null) {
            item.setLoginPassword(null);
            item.setLoginPasswordSaltKey(null);
        }
    }

    private void applyAuditorNames(List<MemberRes.Item> items) {
        displayNameResolver.applyAuditorDisplayNames(
                items,
                MemberRes.Item::getCreatedBy,
                MemberRes.Item::setCreatedBy,
                MemberRes.Item::getUpdatedBy,
                MemberRes.Item::setUpdatedBy
        );
    }
}
