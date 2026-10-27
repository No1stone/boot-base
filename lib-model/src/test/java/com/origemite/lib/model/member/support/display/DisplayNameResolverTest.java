package com.origemite.lib.model.member.support.display;

import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import com.origemite.lib.model.member.persistence.MemberPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DisplayNameResolverTest {

    @Mock
    private MemberPort memberPort;

    @InjectMocks
    private DisplayNameResolver displayNameResolver;

    @Test
    @DisplayName("회원 id를 findAllName(IN)으로 loginId 맵을 만든다")
    void findMemberNameMap() {
        MemberRes.Name member = new MemberRes.Name();
        member.setId("m1");
        member.setLoginId("user@test.com");
        when(memberPort.findAllName(any())).thenReturn(List.of(member));

        Map<String, String> map = displayNameResolver.findMemberNameMap(List.of("m1"));

        assertThat(map).containsEntry("m1", "user@test.com");
        ArgumentCaptor<MemberReq.Filter> filterCaptor = ArgumentCaptor.forClass(MemberReq.Filter.class);
        verify(memberPort).findAllName(filterCaptor.capture());
        assertThat(filterCaptor.getValue().getIds()).containsExactly("m1");
        verify(memberPort, never()).findByIds(any());
    }

    @Test
    @DisplayName("createdBy·updatedBy PK를 표시명으로 치환한다")
    void applyAuditorDisplayNames() {
        MemberRes.Name member = new MemberRes.Name();
        member.setId("m1");
        member.setLoginId("alice");
        when(memberPort.findAllName(any())).thenReturn(List.of(member));

        MemberRes.Item row = new MemberRes.Item();
        row.setId("row1");
        row.setCreatedBy("m1");
        row.setUpdatedBy("m1");
        List<MemberRes.Item> items = new ArrayList<>(List.of(row));

        displayNameResolver.applyAuditorDisplayNames(
                items,
                MemberRes.Item::getCreatedBy,
                MemberRes.Item::setCreatedBy,
                MemberRes.Item::getUpdatedBy,
                MemberRes.Item::setUpdatedBy
        );

        assertThat(row.getCreatedBy()).isEqualTo("alice");
        assertThat(row.getUpdatedBy()).isEqualTo("alice");
    }

    @Test
    @DisplayName("빈 목록이면 Port를 호출하지 않는다")
    void emptySkipsPort() {
        displayNameResolver.applyAuditorDisplayNames(
                List.of(),
                MemberRes.Item::getCreatedBy,
                MemberRes.Item::setCreatedBy,
                MemberRes.Item::getUpdatedBy,
                MemberRes.Item::setUpdatedBy
        );
        verify(memberPort, never()).findAllName(any());
        verify(memberPort, never()).findByIds(any());
    }

    @Test
    @DisplayName("맵에 없으면 id를 그대로 둔다")
    void keepsIdWhenMissing() {
        when(memberPort.findAllName(any())).thenReturn(List.of());

        MemberRes.Item row = new MemberRes.Item();
        row.setCreatedBy("unknown");
        displayNameResolver.applyCreatedByDisplayNames(
                List.of(row),
                MemberRes.Item::getCreatedBy,
                MemberRes.Item::setCreatedBy
        );

        assertThat(row.getCreatedBy()).isEqualTo("unknown");
    }
}
