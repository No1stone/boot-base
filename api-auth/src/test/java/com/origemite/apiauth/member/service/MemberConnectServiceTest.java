package com.origemite.apiauth.member.service;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.ModelMapperUtil;
import com.origemite.lib.model.auth.dto.MemberConnectReq;
import com.origemite.lib.model.auth.dto.MemberConnectRes;
import com.origemite.lib.model.auth.entity.MemberConnect;
import com.origemite.lib.model.auth.repository.MemberConnectRepository;
import com.origemite.lib.model.auth.service.MemberConnectService;
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
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MemberConnectServiceTest {

    @InjectMocks
    MemberConnectService memberConnectService;

    @Mock
    MemberConnectRepository memberConnectRepository;

    @Test
    @DisplayName("MemberConnectService - search 성공")
    void search_success() {
        // given
        MemberConnectReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberConnect entity = sampleEntity();
        Page<MemberConnect> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberConnectRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberConnect.class))
                  .thenReturn(entity);
            given(memberConnectRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberConnectRes.Item.class))
                  .thenReturn(itemPage);

            // when
            Page<MemberConnectRes.Item> result = memberConnectService.search(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberConnectRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberConnectService - save 성공")
    void save_success() {
        // given
        MemberConnectReq.Create create = sampleCreate();
        MemberConnect entity = sampleEntity();
        MemberConnectRes.Id idRes = sampleIdRes();

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(create, MemberConnect.class))
                  .thenReturn(entity);
            given(memberConnectRepository.save(any(MemberConnect.class)))
                    .willReturn(entity);
            mocked.when(() -> ModelMapperUtil.map(entity, MemberConnectRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberConnectRes.Id result = memberConnectService.save(create);

            // then
            assertNotNull(result);
            verify(memberConnectRepository).save(any(MemberConnect.class));
        }
    }

    @Test
    @DisplayName("MemberConnectService - findById 성공")
    void findById_success() {
        // given
        Integer id = sampleId();
        MemberConnect entity = sampleEntity();
        MemberConnectRes.Item item = sampleItem();

        given(memberConnectRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberConnectRes.Item.class))
                  .thenReturn(item);

            // when
            MemberConnectRes.Item result = memberConnectService.findById(id);

            // then
            assertNotNull(result);
            verify(memberConnectRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberConnectService - saveById 성공")
    void saveById_success() {
        // given
        Integer id = sampleId();
        MemberConnectReq.Update update = sampleUpdate();
        MemberConnect entity = sampleEntity();
        MemberConnectRes.Id idRes = sampleIdRes();

        given(memberConnectRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberConnectRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberConnectRes.Id result = memberConnectService.saveById(id, update);

            // then
            assertNotNull(result);
            verify(memberConnectRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberConnectService - deleteById 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();
        MemberConnect entity = sampleEntity();
        given(memberConnectRepository.findById(id)).willReturn(Optional.of(entity));

        // when
        memberConnectService.deleteById(id);

        // then
        verify(memberConnectRepository).findById(id);
    }

    @Test
    @DisplayName("MemberConnectService - findAll 성공")
    void findAll_success() {
        // given
        MemberConnectReq.Filter filter = sampleFilter();
        MemberConnect entity = sampleEntity();
        Page<MemberConnect> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberConnectRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberConnect.class))
                  .thenReturn(entity);
            given(memberConnectRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberConnectRes.Item.class))
                  .thenReturn(itemPage);

            // when
            var result = memberConnectService.findAll(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberConnectRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberConnectService - findByIds 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberConnect> entities = List.of(sampleEntity());
        List<MemberConnectRes.Item> items = List.of(sampleItem());

        given(memberConnectRepository.findByIdIn(ids)).willReturn(entities);
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.mapAll(entities, MemberConnectRes.Item.class))
                  .thenReturn(items);

            // when
            var result = memberConnectService.findByIds(ids);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberConnectRepository).findByIdIn(ids);
        }
    }

    @Test
    @DisplayName("MemberConnectService - searchName 성공")
    void searchName_success() {
        // given
        MemberConnectReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberConnect entity = sampleEntity();
        Page<MemberConnect> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberConnectRes.Name> namePage = new PageImpl<>(List.of(sampleName()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberConnect.class))
                  .thenReturn(entity);
            given(memberConnectRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberConnectRes.Name.class))
                  .thenReturn(namePage);

            // when
            Page<MemberConnectRes.Name> result = memberConnectService.searchName(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberConnectRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberConnectService - findAllName 성공")
    void findAllName_success() {
        // given
        MemberConnectReq.Filter filter = sampleFilter();
        MemberConnect entity = sampleEntity();
        Page<MemberConnect> entityPage = new PageImpl<>(List.of(entity));
        List<MemberConnectRes.Name> names = List.of(sampleName());

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberConnect.class))
                  .thenReturn(entity);
            given(memberConnectRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage.stream().toList(), MemberConnectRes.Name.class))
                  .thenReturn(names);

            // when
            var result = memberConnectService.findAllName(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberConnectRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberConnectService - deleteByIds 성공")
    void deleteByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberConnect> entities = List.of(sampleEntity());
        given(memberConnectRepository.findByIdIn(ids)).willReturn(entities);

        // when
        memberConnectService.deleteByIds(ids);

        // then
        verify(memberConnectRepository).findByIdIn(ids);
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberConnect sampleEntity() {
        MemberConnect entity = MemberConnect.builder().build();
        entity.setId(sampleId());
        entity.setMemberId("member_id-sample");
        entity.setConnectTypeCode("connect_type_code-sample");
        entity.setConnectedId("connected_id-sample");
        entity.setName("name-sample");
        entity.setEmail("email-sample");
        entity.setMobile("mobile-sample");
        entity.setPicture("picture-sample");
        entity.setGender("gender-sample");
        entity.setAge("age-sample");
        entity.setBirthday("birthday-sample");
        entity.setBirthyear("birthyear-sample");
        entity.setLocale("locale-sample");
        entity.setCipherKeyId("cipher_key_id-sample");
        entity.setStatus("status-sample");
        return entity;
    }

    private MemberConnectRes.Id sampleIdRes() {
        MemberConnectRes.Id id = new MemberConnectRes.Id();
        id.setId(sampleId());
        return id;
    }

    private MemberConnectRes.Item sampleItem() {
        MemberConnectRes.Item item = new MemberConnectRes.Item();
        item.setId(sampleId());
        item.setMemberId("member_id-sample");
        item.setConnectTypeCode("connect_type_code-sample");
        item.setConnectedId("connected_id-sample");
        item.setName("name-sample");
        item.setEmail("email-sample");
        item.setMobile("mobile-sample");
        item.setPicture("picture-sample");
        item.setGender("gender-sample");
        item.setAge("age-sample");
        item.setBirthday("birthday-sample");
        item.setBirthyear("birthyear-sample");
        item.setLocale("locale-sample");
        item.setCipherKeyId("cipher_key_id-sample");
        item.setStatus("status-sample");
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy("updated_by-sample");
        item.setVersion(1);
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("created_by-sample");
        return item;
    }

    private MemberConnectRes.Name sampleName() {
        MemberConnectRes.Name name = new MemberConnectRes.Name();
        // TODO: name 필드에 맞게 필요시 값 세팅
        return name;
    }

    private MemberConnectReq.Filter sampleFilter() {
        MemberConnectReq.Filter.FilterBuilder builder = MemberConnectReq.Filter.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.connectTypeCode("connect_type_code-sample");
        builder.connectedId("connected_id-sample");
        builder.name("name-sample");
        builder.email("email-sample");
        builder.mobile("mobile-sample");
        builder.picture("picture-sample");
        builder.gender("gender-sample");
        builder.age("age-sample");
        builder.birthday("birthday-sample");
        builder.birthyear("birthyear-sample");
        builder.locale("locale-sample");
        builder.cipherKeyId("cipher_key_id-sample");
        builder.status("status-sample");
        builder.updatedAt(LocalDateTime.now());
        builder.updatedBy("updated_by-sample");
        builder.version(1);
        builder.createdAt(LocalDateTime.now());
        builder.createdBy("created_by-sample");
        return builder.build();
    }

    private MemberConnectReq.Create sampleCreate() {
        MemberConnectReq.Create.CreateBuilder builder = MemberConnectReq.Create.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.connectTypeCode("connect_type_code-sample");
        builder.connectedId("connected_id-sample");
        builder.name("name-sample");
        builder.email("email-sample");
        builder.mobile("mobile-sample");
        builder.picture("picture-sample");
        builder.gender("gender-sample");
        builder.age("age-sample");
        builder.birthday("birthday-sample");
        builder.birthyear("birthyear-sample");
        builder.locale("locale-sample");
        builder.cipherKeyId("cipher_key_id-sample");
        builder.status("status-sample");
        return builder.build();
    }

    private MemberConnectReq.Update sampleUpdate() {
        MemberConnectReq.Update.UpdateBuilder builder = MemberConnectReq.Update.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.connectTypeCode("connect_type_code-sample");
        builder.connectedId("connected_id-sample");
        builder.name("name-sample");
        builder.email("email-sample");
        builder.mobile("mobile-sample");
        builder.picture("picture-sample");
        builder.gender("gender-sample");
        builder.age("age-sample");
        builder.birthday("birthday-sample");
        builder.birthyear("birthyear-sample");
        builder.locale("locale-sample");
        builder.cipherKeyId("cipher_key_id-sample");
        builder.status("status-sample");
        return builder.build();
    }

}
