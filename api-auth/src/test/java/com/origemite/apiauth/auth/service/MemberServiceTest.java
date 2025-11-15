package com.origemite.apiauth.auth.service;

import com.origemite.lib.common.util.ModelMapperUtil;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.model.auth.entity.Member;
import com.origemite.lib.model.auth.repository.MemberRepository;
import com.origemite.lib.model.auth.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    MemberService memberService;

    @Mock
    MemberRepository memberRepository;

    @Test
    @DisplayName("MemberService - search 성공")
    void search_success() {
        // given
        MemberReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Member entity = sampleEntity();
        Page<Member> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, Member.class))
                    .thenReturn(entity);
            given(memberRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberRes.Item.class))
                    .thenReturn(itemPage);

            // when
            Page<MemberRes.Item> result = memberService.search(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberService - save 성공")
    void save_success() {
        // given
        MemberReq.Create create = sampleCreate();
        Member entity = sampleEntity();
        MemberRes.Id idRes = sampleIdRes();

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(create, Member.class))
                    .thenReturn(entity);
            given(memberRepository.save(any(Member.class)))
                    .willReturn(entity);
            mocked.when(() -> ModelMapperUtil.map(entity, MemberRes.Id.class))
                    .thenReturn(idRes);

            // when
            MemberRes.Id result = memberService.save(create);

            // then
            assertNotNull(result);
            verify(memberRepository).save(any(Member.class));
        }
    }

    @Test
    @DisplayName("MemberService - findById 성공")
    void findById_success() {
        // given
        String id = sampleId();
        Member entity = sampleEntity();
        MemberRes.Item item = sampleItem();

        given(memberRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberRes.Item.class))
                    .thenReturn(item);

            // when
            MemberRes.Item result = memberService.findById(id);

            // then
            assertNotNull(result);
            verify(memberRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberService - saveById 성공")
    void saveById_success() {
        // given
        String id = sampleId();
        MemberReq.Update update = sampleUpdate();
        Member entity = sampleEntity();
        MemberRes.Id idRes = sampleIdRes();

        given(memberRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberRes.Id.class))
                    .thenReturn(idRes);

            // when
            MemberRes.Id result = memberService.saveById(id, update);

            // then
            assertNotNull(result);
            verify(memberRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberService - deleteById 성공")
    void deleteById_success() {
        // given
        String id = sampleId();
        Member entity = sampleEntity();
        given(memberRepository.findById(id)).willReturn(Optional.of(entity));

        // when
        memberService.deleteById(id);

        // then
        verify(memberRepository).findById(id);
    }

    @Test
    @DisplayName("MemberService - findAll 성공")
    void findAll_success() {
        // given
        MemberReq.Filter filter = sampleFilter();
        Member entity = sampleEntity();
        Page<Member> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, Member.class))
                    .thenReturn(entity);
            given(memberRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberRes.Item.class))
                    .thenReturn(itemPage);

            // when
            var result = memberService.findAll(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberService - findByIds 성공")
    void findByIds_success() {
        // given
        List<String> ids = List.of(sampleId());
        List<Member> entities = List.of(sampleEntity());
        List<MemberRes.Item> items = List.of(sampleItem());

        given(memberRepository.findByIdIn(ids)).willReturn(entities);
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.mapAll(entities, MemberRes.Item.class))
                    .thenReturn(items);

            // when
            var result = memberService.findByIds(ids);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberRepository).findByIdIn(ids);
        }
    }

    @Test
    @DisplayName("MemberService - searchName 성공")
    void searchName_success() {
        // given
        MemberReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Member entity = sampleEntity();
        Page<Member> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberRes.Name> namePage = new PageImpl<>(List.of(sampleName()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, Member.class))
                    .thenReturn(entity);
            given(memberRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberRes.Name.class))
                    .thenReturn(namePage);

            // when
            Page<MemberRes.Name> result = memberService.searchName(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberService - findAllName 성공")
    void findAllName_success() {
        // given
        MemberReq.Filter filter = sampleFilter();
        Member entity = sampleEntity();
        Page<Member> entityPage = new PageImpl<>(List.of(entity));
        List<MemberRes.Name> names = List.of(sampleName());

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, Member.class))
                    .thenReturn(entity);
            given(memberRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage.stream().toList(), MemberRes.Name.class))
                    .thenReturn(names);

            // when
            var result = memberService.findAllName(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberService - deleteByIds 성공")
    void deleteByIds_success() {
        // given
        List<String> ids = List.of(sampleId());
        List<Member> entities = List.of(sampleEntity());
        given(memberRepository.findByIdIn(ids)).willReturn(entities);

        // when
        memberService.deleteByIds(ids);

        // then
        verify(memberRepository).findByIdIn(ids);
    }

    private String sampleId() {
        return "sample-id";
    }

    private Member sampleEntity() {
        Member entity = Member.builder().build();
        entity.setId(sampleId());
        entity.setLoginId("login_id-sample");
        entity.setLoginPassword("login_password-sample");
        entity.setLoginPasswordSaltKey("login_password_salt_key-sample");
        entity.setStatus("status-sample");
        return entity;
    }

    private MemberRes.Id sampleIdRes() {
        MemberRes.Id id = new MemberRes.Id();
        id.setId(sampleId());
        return id;
    }

    private MemberRes.Item sampleItem() {
        MemberRes.Item item = new MemberRes.Item();
        item.setId(sampleId());
        item.setLoginId("login_id-sample");
        item.setLoginPassword("login_password-sample");
        item.setLoginPasswordSaltKey("login_password_salt_key-sample");
        item.setStatus("status-sample");
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy("updated_by-sample");
        item.setVersion(1);
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("created_by-sample");
        return item;
    }

    private MemberRes.Name sampleName() {
        MemberRes.Name name = new MemberRes.Name();
        // TODO: name 필드에 맞게 필요시 값 세팅
        return name;
    }

    private MemberReq.Filter sampleFilter() {
        MemberReq.Filter.FilterBuilder builder = MemberReq.Filter.builder();
        builder.id(sampleId());
        builder.loginId("login_id-sample");
        builder.loginPassword("login_password-sample");
        builder.loginPasswordSaltKey("login_password_salt_key-sample");
        builder.status("status-sample");
        builder.updatedAt(LocalDateTime.now());
        builder.updatedBy("updated_by-sample");
        builder.version(1);
        builder.createdAt(LocalDateTime.now());
        builder.createdBy("created_by-sample");
        return builder.build();
    }

    private MemberReq.Create sampleCreate() {
        MemberReq.Create.CreateBuilder builder = MemberReq.Create.builder();
        builder.id(sampleId());
        builder.loginId("login_id-sample");
        builder.loginPassword("login_password-sample");
        builder.loginPasswordSaltKey("login_password_salt_key-sample");
        builder.status("status-sample");
        return builder.build();
    }

    private MemberReq.Update sampleUpdate() {
        MemberReq.Update.UpdateBuilder builder = MemberReq.Update.builder();
        builder.id(sampleId());
        builder.loginId("login_id-sample");
        builder.loginPassword("login_password-sample");
        builder.loginPasswordSaltKey("login_password_salt_key-sample");
        builder.status("status-sample");
        return builder.build();
    }

}
