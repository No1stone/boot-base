package com.origemite.lib.model.member.facade;

import com.origemite.lib.cache.CachePort;
import com.origemite.lib.common.util.TransformUtils;
import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import com.origemite.lib.model.member.persistence.MemberPort;
import com.origemite.lib.model.member.support.display.DisplayNameResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberFacadeTest {

    @Mock
    private MemberPort memberPort;

    @Mock
    private DisplayNameResolver displayNameResolver;

    @Mock
    private CachePort cachePort;

    @InjectMocks
    private MemberFacade memberFacade;

    @Test
    @DisplayName("save는 솔트와 비밀번호 해시를 넣은 뒤 Port에 위임한다")
    void saveHashesPasswordBeforePort() {
        MemberReq.Create create = MemberReq.Create.builder()
                .id("m1")
                .loginId("user@test.com")
                .loginPassword("plain-password")
                .status("A")
                .build();
        MemberRes.Id idRes = new MemberRes.Id();
        idRes.setId("m1");
        when(memberPort.save(any())).thenReturn(idRes);

        MemberRes.Id result = memberFacade.save(create);

        assertThat(result.getId()).isEqualTo("m1");
        ArgumentCaptor<MemberReq.Create> captor = ArgumentCaptor.forClass(MemberReq.Create.class);
        verify(memberPort).save(captor.capture());
        MemberReq.Create saved = captor.getValue();
        assertThat(saved.getLoginPasswordSaltKey()).isNotBlank();
        assertThat(saved.getLoginPassword()).isNotEqualTo("plain-password");
        assertThat(saved.getLoginPassword().length()).isGreaterThan(20);
        verify(cachePort).delete(MemberFacade.ITEM_CACHE_KEY_PREFIX + "m1");
    }

    @Test
    @DisplayName("search 후 DisplayNameResolver로 감사 필드를 치환한다")
    void searchAppliesDisplayNames() {
        MemberRes.Item item = new MemberRes.Item();
        item.setId("m1");
        item.setCreatedBy("actor");
        when(memberPort.search(any(), any())).thenReturn(new PageImpl<>(List.of(item)));

        memberFacade.search(new MemberReq.Filter(), PageRequest.of(0, 10));

        verify(displayNameResolver).applyAuditorDisplayNames(
                anyList(),
                any(),
                any(),
                any(),
                any()
        );
    }

    @Test
    @DisplayName("findById 캐시 미스면 Port 조회 후 캐시에 넣고 표시명을 적용한다")
    void findByIdCacheMissLoadsAndPuts() {
        MemberRes.Item item = new MemberRes.Item();
        item.setId("m1");
        item.setLoginId("user@test.com");
        item.setLoginPassword("secret");
        when(cachePort.get(MemberFacade.ITEM_CACHE_KEY_PREFIX + "m1")).thenReturn(null);
        when(memberPort.findById("m1")).thenReturn(item);

        assertThat(memberFacade.findById("m1").getId()).isEqualTo("m1");

        verify(memberPort).findById("m1");
        ArgumentCaptor<String> jsonCaptor = ArgumentCaptor.forClass(String.class);
        verify(cachePort).set(
                eq(MemberFacade.ITEM_CACHE_KEY_PREFIX + "m1"),
                jsonCaptor.capture(),
                eq(Duration.ofMinutes(30))
        );
        MemberRes.Item cached = TransformUtils.parse(jsonCaptor.getValue(), MemberRes.Item.class);
        assertThat(cached.getLoginPassword()).isNull();
        assertThat(cached.getLoginPasswordSaltKey()).isNull();
        verify(displayNameResolver).applyAuditorDisplayNames(eq(List.of(item)), any(), any(), any(), any());
    }

    @Test
    @DisplayName("findById 캐시 히트면 Port를 호출하지 않는다")
    void findByIdCacheHitSkipsPort() {
        MemberRes.Item cached = new MemberRes.Item();
        cached.setId("m1");
        cached.setLoginId("user@test.com");
        when(cachePort.get(MemberFacade.ITEM_CACHE_KEY_PREFIX + "m1"))
                .thenReturn(TransformUtils.toString(cached));

        assertThat(memberFacade.findById("m1").getLoginId()).isEqualTo("user@test.com");

        verify(memberPort, never()).findById(anyString());
        verify(cachePort, never()).set(anyString(), anyString(), any());
    }

    @Test
    @DisplayName("saveById·deleteById는 캐시를 무효화한다")
    void mutationsEvictCache() {
        MemberRes.Id idRes = new MemberRes.Id();
        idRes.setId("m1");
        when(memberPort.saveById(eq("m1"), any())).thenReturn(idRes);

        memberFacade.saveById("m1", MemberReq.Update.builder().build());
        memberFacade.deleteById("m1");

        verify(cachePort, org.mockito.Mockito.times(2)).delete(MemberFacade.ITEM_CACHE_KEY_PREFIX + "m1");
    }
}
