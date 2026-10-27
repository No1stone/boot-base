package com.origemite.apiauth.member.facade;

import com.origemite.apiauth.support.display.DisplayNameResolver;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.model.auth.persistence.MemberPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberFacadeTest {

    @Mock
    private MemberPort memberPort;

    @Mock
    private DisplayNameResolver displayNameResolver;

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
    @DisplayName("findById는 Port 조회 후 표시명을 적용한다")
    void findByIdAppliesDisplayNames() {
        MemberRes.Item item = new MemberRes.Item();
        item.setId("m1");
        when(memberPort.findById("m1")).thenReturn(item);

        assertThat(memberFacade.findById("m1").getId()).isEqualTo("m1");
        verify(memberPort).findById("m1");
        verify(displayNameResolver).applyAuditorDisplayNames(eq(List.of(item)), any(), any(), any(), any());
    }
}
