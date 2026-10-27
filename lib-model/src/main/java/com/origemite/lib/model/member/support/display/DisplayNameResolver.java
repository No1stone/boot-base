package com.origemite.lib.model.member.support.display;

import com.origemite.lib.model.member.dto.MemberRes;
import com.origemite.lib.model.member.persistence.MemberPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Facade 유스케이스: 저장된 PK(감사·FK)를 Port Name/조회 계약으로 표시명에 치환한다.
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DisplayNameResolver {

    private final MemberPort memberPort;

    /**
     * 회원 id → 표시명(loginId). Port findByIds 결과를 사용한다.
     */
    public Map<String, String> findMemberNameMap(List<String> ids) {
        List<String> distinctIds = distinctIds(ids);
        if (distinctIds.isEmpty()) {
            return Collections.emptyMap();
        }
        return memberPort.findByIds(distinctIds).stream()
                .collect(Collectors.toMap(
                        MemberRes.Item::getId,
                        DisplayNameResolver::toMemberDisplayName,
                        (left, right) -> left
                ));
    }

    public <T> void applyAuditorDisplayNames(
            Page<T> page,
            Function<T, String> createdByGetter,
            BiConsumer<T, String> createdBySetter,
            Function<T, String> updatedByGetter,
            BiConsumer<T, String> updatedBySetter
    ) {
        applyAuditorDisplayNames(
                page.getContent(),
                createdByGetter,
                createdBySetter,
                updatedByGetter,
                updatedBySetter
        );
    }

    public <T> void applyAuditorDisplayNames(
            List<T> items,
            Function<T, String> createdByGetter,
            BiConsumer<T, String> createdBySetter,
            Function<T, String> updatedByGetter,
            BiConsumer<T, String> updatedBySetter
    ) {
        if (items == null || items.isEmpty()) {
            return;
        }
        List<String> auditorIds = new ArrayList<>();
        for (T item : items) {
            collectId(auditorIds, createdByGetter.apply(item));
            collectId(auditorIds, updatedByGetter.apply(item));
        }
        applyAuditorDisplayNames(
                items,
                findMemberNameMap(auditorIds),
                createdByGetter,
                createdBySetter,
                updatedByGetter,
                updatedBySetter
        );
    }

    public <T> void applyAuditorDisplayNames(
            List<T> items,
            Map<String, String> nameMap,
            Function<T, String> createdByGetter,
            BiConsumer<T, String> createdBySetter,
            Function<T, String> updatedByGetter,
            BiConsumer<T, String> updatedBySetter
    ) {
        if (items == null || items.isEmpty()) {
            return;
        }
        Map<String, String> safeMap = nameMap != null ? nameMap : Collections.emptyMap();
        applyResolvedName(items, createdByGetter, createdBySetter, safeMap);
        applyResolvedName(items, updatedByGetter, updatedBySetter, safeMap);
    }

    public <T> void applyCreatedByDisplayNames(
            List<T> items,
            Function<T, String> createdByGetter,
            BiConsumer<T, String> createdBySetter
    ) {
        if (items == null || items.isEmpty()) {
            return;
        }
        List<String> auditorIds = new ArrayList<>();
        for (T item : items) {
            collectId(auditorIds, createdByGetter.apply(item));
        }
        applyResolvedName(items, createdByGetter, createdBySetter, findMemberNameMap(auditorIds));
    }

    private static String toMemberDisplayName(MemberRes.Item member) {
        if (StringUtils.hasText(member.getLoginId())) {
            return member.getLoginId().trim();
        }
        return member.getId();
    }

    private static <T> void applyResolvedName(
            List<T> items,
            Function<T, String> getter,
            BiConsumer<T, String> setter,
            Map<String, String> nameMap
    ) {
        for (T item : items) {
            String id = getter.apply(item);
            if (StringUtils.hasText(id)) {
                setter.accept(item, nameMap.getOrDefault(id, id));
            }
        }
    }

    private static void collectId(List<String> ids, String id) {
        if (StringUtils.hasText(id)) {
            ids.add(id);
        }
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
}
