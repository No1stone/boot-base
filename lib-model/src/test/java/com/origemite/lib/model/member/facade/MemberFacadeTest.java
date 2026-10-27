package com.origemite.lib.model.member.facade;

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
    @DisplayName("findById는 Port 조회 후 비밀값을 제거하고 표시명을 적용한다")
    void findByIdAppliesDisplayNamesAndStripsSecrets() {
        MemberRes.Item item = new MemberRes.Item();
        item.setId("m1");
        item.setLoginPassword("secret");
        item.setLoginPasswordSaltKey("salt");
        when(memberPort.findById("m1")).thenReturn(item);

        MemberRes.Item result = memberFacade.findById("m1");

        assertThat(result.getId()).isEqualTo("m1");
        assertThat(result.getLoginPassword()).isNull();
        assertThat(result.getLoginPasswordSaltKey()).isNull();
        verify(memberPort).findById("m1");
        verify(displayNameResolver).applyAuditorDisplayNames(eq(List.of(item)), any(), any(), any(), any());
    }
}
