package com.origemite.apiauth.member.persistence;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.web.ResponseType;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.model.auth.entity.Member;
import com.origemite.lib.model.auth.persistence.MemberAdapter;
import com.origemite.lib.model.auth.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberAdapterTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberAdapter memberAdapter;

    @Test
    @DisplayName("search는 Spec으로 조회한다")
    void searchUsesSpecification() {
        MemberReq.Filter filter = MemberReq.Filter.builder().loginId("user@test.com").build();
        Pageable pageable = PageRequest.of(0, 10);
        Member entity = member("m1", "user@test.com");
        when(memberRepository.findAll(any(Specification.class), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(entity)));

        Page<MemberRes.Item> result = memberAdapter.search(filter, pageable);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent()).extracting(MemberRes.Item::getId).containsExactly("m1");
        verify(memberRepository).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    @DisplayName("경계: findByIds에 null이면 조회하지 않고 빈 목록이다")
    void findByIdsNullSkipsQuery() {
        assertThat(memberAdapter.findByIds(null)).isEmpty();
        verify(memberRepository, never()).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("경계: findByIds에 빈 목록이면 조회하지 않는다")
    void findByIdsEmptySkipsQuery() {
        assertThat(memberAdapter.findByIds(List.of())).isEmpty();
        verify(memberRepository, never()).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("findByIds는 Filter.ids로 Spec 조회한다")
    void findByIdsUsesSpec() {
        when(memberRepository.findAll(any(Specification.class)))
                .thenReturn(List.of(member("a", "a@test.com"), member("b", "b@test.com")));

        List<MemberRes.Item> result = memberAdapter.findByIds(List.of("a", "b"));

        assertThat(result).hasSize(2);
        verify(memberRepository).findAll(any(Specification.class));
    }

    @Test
    @DisplayName("경계: saveAll에 빈 목록이면 저장하지 않는다")
    void saveAllEmptySkipsSave() {
        assertThat(memberAdapter.saveAll(List.of())).isEmpty();
        verify(memberRepository, never()).saveAll(any());
    }

    @Test
    @DisplayName("saveAll은 일괄 저장한다")
    void saveAllPersists() {
        MemberReq.Create create = MemberReq.Create.builder()
                .id("m1")
                .loginId("user@test.com")
                .status("A")
                .build();
        when(memberRepository.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        List<MemberRes.Id> result = memberAdapter.saveAll(List.of(create));

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().getId()).isEqualTo("m1");
        verify(memberRepository).saveAll(any());
    }

    @Test
    @DisplayName("실패: 없는 아이디 조회는 NOT_FOUND_RESOURCE다")
    void findByIdMissingThrows() {
        when(memberRepository.findById("missing")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> memberAdapter.findById("missing"))
                .isInstanceOf(BizErrorException.class)
                .extracting(ex -> ((BizErrorException) ex).getResponseType())
                .isEqualTo(ResponseType.NOT_FOUND_RESOURCE);
    }

    @Test
    @DisplayName("deleteByIds는 Spec으로 조회 후 soft delete한다")
    void deleteByIdsUsesSpec() {
        Member entity = member("a", "a@test.com");
        when(memberRepository.findAll(any(Specification.class))).thenReturn(List.of(entity));

        memberAdapter.deleteByIds(List.of("a"));

        ArgumentCaptor<Specification<Member>> captor = ArgumentCaptor.forClass(Specification.class);
        verify(memberRepository).findAll(captor.capture());
        assertThat(captor.getValue()).isNotNull();
    }

    private static Member member(String id, String loginId) {
        return Member.builder()
                .id(id)
                .loginId(loginId)
                .loginPassword("hash")
                .loginPasswordSaltKey("salt")
                .status("A")
                .build();
    }
}
