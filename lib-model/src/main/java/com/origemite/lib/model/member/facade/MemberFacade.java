package com.origemite.lib.model.member.facade;

import com.origemite.lib.cache.CachePort;
import com.origemite.lib.common.util.EncUtils;
import com.origemite.lib.common.util.Salt;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import com.origemite.lib.model.member.persistence.MemberPort;
import com.origemite.lib.model.member.support.display.DisplayNameResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberFacade {

    static final String ITEM_CACHE_KEY_PREFIX = "member:item:";
    static final Duration ITEM_CACHE_TTL = Duration.ofMinutes(30);

    private final MemberPort memberPort;
    private final DisplayNameResolver displayNameResolver;
    private final CachePort cachePort;

    public Page<MemberRes.Item> search(MemberReq.Filter filter, Pageable pageable) {
        Page<MemberRes.Item> page = memberPort.search(filter, pageable);
        applyAuditorNames(page.getContent());
        return page;
    }

    @Transactional
    public MemberRes.Id save(MemberReq.Create create) {
        String salt = Salt.generate();
        create.setLoginPasswordSaltKey(salt);
        create.setLoginPassword(EncUtils.encSHA512(create.getLoginPassword(), salt));
        MemberRes.Id id = memberPort.save(create);
        evictItem(id.getId());
        return id;
    }

    public MemberRes.Item findById(String id) {
        MemberRes.Item item = getItemCacheAside(id);
        applyAuditorNames(List.of(item));
        return item;
    }

    @Transactional
    public MemberRes.Id saveById(String id, MemberReq.Update update) {
        MemberRes.Id saved = memberPort.saveById(id, update);
        evictItem(id);
        return saved;
    }

    @Transactional
    public void deleteById(String id) {
        memberPort.deleteById(id);
        evictItem(id);
    }

    public List<MemberRes.Item> findAll(MemberReq.Filter filter) {
        List<MemberRes.Item> items = memberPort.findAll(filter);
        applyAuditorNames(items);
        return items;
    }

    public List<MemberRes.Item> findByIds(List<String> ids) {
        List<String> distinctIds = distinctIds(ids);
        if (distinctIds.isEmpty()) {
            return List.of();
        }

        Map<String, MemberRes.Item> byId = new LinkedHashMap<>();
        List<String> missing = new ArrayList<>();
        for (String id : distinctIds) {
            MemberRes.Item cached = readItemCache(id);
            if (cached != null) {
                byId.put(id, cached);
            } else {
                missing.add(id);
            }
        }

        if (!missing.isEmpty()) {
            for (MemberRes.Item loaded : memberPort.findByIds(missing)) {
                putItemCache(loaded);
                byId.put(loaded.getId(), loaded);
            }
        }

        List<MemberRes.Item> items = distinctIds.stream()
                .map(byId::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
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
    public void deleteByIds(List<String> ids) {
        memberPort.deleteByIds(ids);
        if (ids != null) {
            ids.forEach(this::evictItem);
        }
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

    private MemberRes.Item getItemCacheAside(String id) {
        MemberRes.Item cached = readItemCache(id);
        if (cached != null) {
            return cached;
        }
        MemberRes.Item loaded = memberPort.findById(id);
        putItemCache(loaded);
        return loaded;
    }

    private MemberRes.Item readItemCache(String id) {
        if (!StringUtils.hasText(id)) {
            return null;
        }
        String json = cachePort.get(itemCacheKey(id));
        if (!StringUtils.hasText(json)) {
            return null;
        }
        return TransformUtils.parse(json, MemberRes.Item.class);
    }

    private void putItemCache(MemberRes.Item item) {
        if (item == null || !StringUtils.hasText(item.getId())) {
            return;
        }
        MemberRes.Item toCache = copyWithoutSecrets(item);
        cachePort.set(itemCacheKey(item.getId()), TransformUtils.toString(toCache), ITEM_CACHE_TTL);
    }

    private void evictItem(String id) {
        if (StringUtils.hasText(id)) {
            cachePort.delete(itemCacheKey(id));
        }
    }

    private static String itemCacheKey(String id) {
        return ITEM_CACHE_KEY_PREFIX + id;
    }

    private static MemberRes.Item copyWithoutSecrets(MemberRes.Item source) {
        MemberRes.Item copy = TransformUtils.parse(TransformUtils.toString(source), MemberRes.Item.class);
        if (copy != null) {
            copy.setLoginPassword(null);
            copy.setLoginPasswordSaltKey(null);
        }
        return copy;
    }

    private static List<String> distinctIds(List<String> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return ids.stream()
                .filter(StringUtils::hasText)
                .map(String::trim)
                .distinct()
                .toList();
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
