package com.origemite.apiauth.member.service;

import com.origemite.lib.common.util.ModelMapperUtil;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryRes;
import com.origemite.lib.model.auth.entity.MemberPasswordChangeHistory;
import com.origemite.lib.model.auth.repository.MemberPasswordChangeHistoryRepository;
import com.origemite.lib.model.auth.service.MemberPasswordChangeHistoryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberPasswordChangeHistoryServiceTest {

    @InjectMocks
    MemberPasswordChangeHistoryService memberPasswordChangeHistoryService;

    @Mock
    MemberPasswordChangeHistoryRepository memberPasswordChangeHistoryRepository;

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - search 성공")
    void search_success() {
        // given
        MemberPasswordChangeHistoryReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberPasswordChangeHistory entity = sampleEntity();
        Page<MemberPasswordChangeHistory> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberPasswordChangeHistoryRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberPasswordChangeHistory.class))
                  .thenReturn(entity);
            given(memberPasswordChangeHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberPasswordChangeHistoryRes.Item.class))
                  .thenReturn(itemPage);

            // when
            Page<MemberPasswordChangeHistoryRes.Item> result = memberPasswordChangeHistoryService.search(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberPasswordChangeHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - save 성공")
    void save_success() {
        // given
        MemberPasswordChangeHistoryReq.Create create = sampleCreate();
        MemberPasswordChangeHistory entity = sampleEntity();
        MemberPasswordChangeHistoryRes.Id idRes = sampleIdRes();

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(create, MemberPasswordChangeHistory.class))
                  .thenReturn(entity);
            given(memberPasswordChangeHistoryRepository.save(any(MemberPasswordChangeHistory.class)))
                    .willReturn(entity);
            mocked.when(() -> ModelMapperUtil.map(entity, MemberPasswordChangeHistoryRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberPasswordChangeHistoryRes.Id result = memberPasswordChangeHistoryService.save(create);

            // then
            assertNotNull(result);
            verify(memberPasswordChangeHistoryRepository).save(any(MemberPasswordChangeHistory.class));
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - findById 성공")
    void findById_success() {
        // given
        Integer id = sampleId();
        MemberPasswordChangeHistory entity = sampleEntity();
        MemberPasswordChangeHistoryRes.Item item = sampleItem();

        given(memberPasswordChangeHistoryRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberPasswordChangeHistoryRes.Item.class))
                  .thenReturn(item);

            // when
            MemberPasswordChangeHistoryRes.Item result = memberPasswordChangeHistoryService.findById(id);

            // then
            assertNotNull(result);
            verify(memberPasswordChangeHistoryRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - saveById 성공")
    void saveById_success() {
        // given
        Integer id = sampleId();
        MemberPasswordChangeHistoryReq.Update update = sampleUpdate();
        MemberPasswordChangeHistory entity = sampleEntity();
        MemberPasswordChangeHistoryRes.Id idRes = sampleIdRes();

        given(memberPasswordChangeHistoryRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberPasswordChangeHistoryRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberPasswordChangeHistoryRes.Id result = memberPasswordChangeHistoryService.saveById(id, update);

            // then
            assertNotNull(result);
            verify(memberPasswordChangeHistoryRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - deleteById 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();
        MemberPasswordChangeHistory entity = sampleEntity();
        given(memberPasswordChangeHistoryRepository.findById(id)).willReturn(Optional.of(entity));

        // when
        memberPasswordChangeHistoryService.deleteById(id);

        // then
        verify(memberPasswordChangeHistoryRepository).findById(id);
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - findAll 성공")
    void findAll_success() {
        // given
        MemberPasswordChangeHistoryReq.Filter filter = sampleFilter();
        MemberPasswordChangeHistory entity = sampleEntity();
        Page<MemberPasswordChangeHistory> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberPasswordChangeHistoryRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberPasswordChangeHistory.class))
                  .thenReturn(entity);
            given(memberPasswordChangeHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberPasswordChangeHistoryRes.Item.class))
                  .thenReturn(itemPage);

            // when
            var result = memberPasswordChangeHistoryService.findAll(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberPasswordChangeHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - findByIds 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberPasswordChangeHistory> entities = List.of(sampleEntity());
        List<MemberPasswordChangeHistoryRes.Item> items = List.of(sampleItem());

        given(memberPasswordChangeHistoryRepository.findByIdIn(ids)).willReturn(entities);
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.mapAll(entities, MemberPasswordChangeHistoryRes.Item.class))
                  .thenReturn(items);

            // when
            var result = memberPasswordChangeHistoryService.findByIds(ids);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberPasswordChangeHistoryRepository).findByIdIn(ids);
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - searchName 성공")
    void searchName_success() {
        // given
        MemberPasswordChangeHistoryReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberPasswordChangeHistory entity = sampleEntity();
        Page<MemberPasswordChangeHistory> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberPasswordChangeHistoryRes.Name> namePage = new PageImpl<>(List.of(sampleName()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberPasswordChangeHistory.class))
                  .thenReturn(entity);
            given(memberPasswordChangeHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberPasswordChangeHistoryRes.Name.class))
                  .thenReturn(namePage);

            // when
            Page<MemberPasswordChangeHistoryRes.Name> result = memberPasswordChangeHistoryService.searchName(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberPasswordChangeHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - findAllName 성공")
    void findAllName_success() {
        // given
        MemberPasswordChangeHistoryReq.Filter filter = sampleFilter();
        MemberPasswordChangeHistory entity = sampleEntity();
        Page<MemberPasswordChangeHistory> entityPage = new PageImpl<>(List.of(entity));
        List<MemberPasswordChangeHistoryRes.Name> names = List.of(sampleName());

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberPasswordChangeHistory.class))
                  .thenReturn(entity);
            given(memberPasswordChangeHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage.stream().toList(), MemberPasswordChangeHistoryRes.Name.class))
                  .thenReturn(names);

            // when
            var result = memberPasswordChangeHistoryService.findAllName(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberPasswordChangeHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberPasswordChangeHistoryService - deleteByIds 성공")
    void deleteByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberPasswordChangeHistory> entities = List.of(sampleEntity());
        given(memberPasswordChangeHistoryRepository.findByIdIn(ids)).willReturn(entities);

        // when
        memberPasswordChangeHistoryService.deleteByIds(ids);

        // then
        verify(memberPasswordChangeHistoryRepository).findByIdIn(ids);
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberPasswordChangeHistory sampleEntity() {
        MemberPasswordChangeHistory entity = MemberPasswordChangeHistory.builder().build();
        entity.setId(sampleId());
        entity.setMemberId("member_id-sample");
        entity.setLoginPassword("login_password-sample");
        entity.setLoginPasswordSaltKey("login_password_salt_key-sample");
        entity.setChangeAt(LocalDateTime.now());
        entity.setCipherKeyId("cipher_key_id-sample");
        return entity;
    }

    private MemberPasswordChangeHistoryRes.Id sampleIdRes() {
        MemberPasswordChangeHistoryRes.Id id = new MemberPasswordChangeHistoryRes.Id();
        id.setId(sampleId());
        return id;
    }

    private MemberPasswordChangeHistoryRes.Item sampleItem() {
        MemberPasswordChangeHistoryRes.Item item = new MemberPasswordChangeHistoryRes.Item();
        item.setId(sampleId());
        item.setMemberId("member_id-sample");
        item.setLoginPassword("login_password-sample");
        item.setLoginPasswordSaltKey("login_password_salt_key-sample");
        item.setChangeAt(LocalDateTime.now());
        item.setCipherKeyId("cipher_key_id-sample");
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy("updated_by-sample");
        item.setVersion(1);
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("created_by-sample");
        return item;
    }

    private MemberPasswordChangeHistoryRes.Name sampleName() {
        MemberPasswordChangeHistoryRes.Name name = new MemberPasswordChangeHistoryRes.Name();
        // TODO: name 필드에 맞게 필요시 값 세팅
        return name;
    }

    private MemberPasswordChangeHistoryReq.Filter sampleFilter() {
        MemberPasswordChangeHistoryReq.Filter.FilterBuilder builder = MemberPasswordChangeHistoryReq.Filter.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.loginPassword("login_password-sample");
        builder.loginPasswordSaltKey("login_password_salt_key-sample");
        builder.changeAt(LocalDateTime.now());
        builder.cipherKeyId("cipher_key_id-sample");
        builder.updatedAt(LocalDateTime.now());
        builder.updatedBy("updated_by-sample");
        builder.version(1);
        builder.createdAt(LocalDateTime.now());
        builder.createdBy("created_by-sample");
        return builder.build();
    }

    private MemberPasswordChangeHistoryReq.Create sampleCreate() {
        MemberPasswordChangeHistoryReq.Create.CreateBuilder builder = MemberPasswordChangeHistoryReq.Create.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.loginPassword("login_password-sample");
        builder.loginPasswordSaltKey("login_password_salt_key-sample");
        builder.changeAt(LocalDateTime.now());
        builder.cipherKeyId("cipher_key_id-sample");
        return builder.build();
    }

    private MemberPasswordChangeHistoryReq.Update sampleUpdate() {
        MemberPasswordChangeHistoryReq.Update.UpdateBuilder builder = MemberPasswordChangeHistoryReq.Update.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.loginPassword("login_password-sample");
        builder.loginPasswordSaltKey("login_password_salt_key-sample");
        builder.changeAt(LocalDateTime.now());
        builder.cipherKeyId("cipher_key_id-sample");
        return builder.build();
    }

}
