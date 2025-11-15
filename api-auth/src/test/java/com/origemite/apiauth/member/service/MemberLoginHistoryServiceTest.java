package com.origemite.apiauth.member.service;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.ModelMapperUtil;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryRes;
import com.origemite.lib.model.auth.entity.MemberLoginHistory;
import com.origemite.lib.model.auth.repository.MemberLoginHistoryRepository;
import com.origemite.lib.model.auth.service.MemberLoginHistoryService;
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
class MemberLoginHistoryServiceTest {

    @InjectMocks
    MemberLoginHistoryService memberLoginHistoryService;

    @Mock
    MemberLoginHistoryRepository memberLoginHistoryRepository;

    @Test
    @DisplayName("MemberLoginHistoryService - search 성공")
    void search_success() {
        // given
        MemberLoginHistoryReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberLoginHistory entity = sampleEntity();
        Page<MemberLoginHistory> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberLoginHistoryRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberLoginHistory.class))
                  .thenReturn(entity);
            given(memberLoginHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberLoginHistoryRes.Item.class))
                  .thenReturn(itemPage);

            // when
            Page<MemberLoginHistoryRes.Item> result = memberLoginHistoryService.search(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberLoginHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - save 성공")
    void save_success() {
        // given
        MemberLoginHistoryReq.Create create = sampleCreate();
        MemberLoginHistory entity = sampleEntity();
        MemberLoginHistoryRes.Id idRes = sampleIdRes();

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(create, MemberLoginHistory.class))
                  .thenReturn(entity);
            given(memberLoginHistoryRepository.save(any(MemberLoginHistory.class)))
                    .willReturn(entity);
            mocked.when(() -> ModelMapperUtil.map(entity, MemberLoginHistoryRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberLoginHistoryRes.Id result = memberLoginHistoryService.save(create);

            // then
            assertNotNull(result);
            verify(memberLoginHistoryRepository).save(any(MemberLoginHistory.class));
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - findById 성공")
    void findById_success() {
        // given
        Integer id = sampleId();
        MemberLoginHistory entity = sampleEntity();
        MemberLoginHistoryRes.Item item = sampleItem();

        given(memberLoginHistoryRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberLoginHistoryRes.Item.class))
                  .thenReturn(item);

            // when
            MemberLoginHistoryRes.Item result = memberLoginHistoryService.findById(id);

            // then
            assertNotNull(result);
            verify(memberLoginHistoryRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - saveById 성공")
    void saveById_success() {
        // given
        Integer id = sampleId();
        MemberLoginHistoryReq.Update update = sampleUpdate();
        MemberLoginHistory entity = sampleEntity();
        MemberLoginHistoryRes.Id idRes = sampleIdRes();

        given(memberLoginHistoryRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberLoginHistoryRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberLoginHistoryRes.Id result = memberLoginHistoryService.saveById(id, update);

            // then
            assertNotNull(result);
            verify(memberLoginHistoryRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - deleteById 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();
        MemberLoginHistory entity = sampleEntity();
        given(memberLoginHistoryRepository.findById(id)).willReturn(Optional.of(entity));

        // when
        memberLoginHistoryService.deleteById(id);

        // then
        verify(memberLoginHistoryRepository).findById(id);
    }

    @Test
    @DisplayName("MemberLoginHistoryService - findAll 성공")
    void findAll_success() {
        // given
        MemberLoginHistoryReq.Filter filter = sampleFilter();
        MemberLoginHistory entity = sampleEntity();
        Page<MemberLoginHistory> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberLoginHistoryRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberLoginHistory.class))
                  .thenReturn(entity);
            given(memberLoginHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberLoginHistoryRes.Item.class))
                  .thenReturn(itemPage);

            // when
            var result = memberLoginHistoryService.findAll(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberLoginHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - findByIds 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberLoginHistory> entities = List.of(sampleEntity());
        List<MemberLoginHistoryRes.Item> items = List.of(sampleItem());

        given(memberLoginHistoryRepository.findByIdIn(ids)).willReturn(entities);
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.mapAll(entities, MemberLoginHistoryRes.Item.class))
                  .thenReturn(items);

            // when
            var result = memberLoginHistoryService.findByIds(ids);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberLoginHistoryRepository).findByIdIn(ids);
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - searchName 성공")
    void searchName_success() {
        // given
        MemberLoginHistoryReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberLoginHistory entity = sampleEntity();
        Page<MemberLoginHistory> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberLoginHistoryRes.Name> namePage = new PageImpl<>(List.of(sampleName()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberLoginHistory.class))
                  .thenReturn(entity);
            given(memberLoginHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberLoginHistoryRes.Name.class))
                  .thenReturn(namePage);

            // when
            Page<MemberLoginHistoryRes.Name> result = memberLoginHistoryService.searchName(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberLoginHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - findAllName 성공")
    void findAllName_success() {
        // given
        MemberLoginHistoryReq.Filter filter = sampleFilter();
        MemberLoginHistory entity = sampleEntity();
        Page<MemberLoginHistory> entityPage = new PageImpl<>(List.of(entity));
        List<MemberLoginHistoryRes.Name> names = List.of(sampleName());

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberLoginHistory.class))
                  .thenReturn(entity);
            given(memberLoginHistoryRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage.stream().toList(), MemberLoginHistoryRes.Name.class))
                  .thenReturn(names);

            // when
            var result = memberLoginHistoryService.findAllName(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberLoginHistoryRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberLoginHistoryService - deleteByIds 성공")
    void deleteByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberLoginHistory> entities = List.of(sampleEntity());
        given(memberLoginHistoryRepository.findByIdIn(ids)).willReturn(entities);

        // when
        memberLoginHistoryService.deleteByIds(ids);

        // then
        verify(memberLoginHistoryRepository).findByIdIn(ids);
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberLoginHistory sampleEntity() {
        MemberLoginHistory entity = MemberLoginHistory.builder().build();
        entity.setId(sampleId());
        entity.setMemberId("member_id-sample");
        entity.setLoginAt(LocalDateTime.now());
        entity.setLoginHistoryUserAgent("login_history_user_agent-sample");
        entity.setLoginHistoryIp("login_history_ip-sample");
        entity.setLoginSuccessYn("login_success_yn-sample");
        return entity;
    }

    private MemberLoginHistoryRes.Id sampleIdRes() {
        MemberLoginHistoryRes.Id id = new MemberLoginHistoryRes.Id();
        id.setId(sampleId());
        return id;
    }

    private MemberLoginHistoryRes.Item sampleItem() {
        MemberLoginHistoryRes.Item item = new MemberLoginHistoryRes.Item();
        item.setId(sampleId());
        item.setMemberId("member_id-sample");
        item.setLoginAt(LocalDateTime.now());
        item.setLoginHistoryUserAgent("login_history_user_agent-sample");
        item.setLoginHistoryIp("login_history_ip-sample");
        item.setLoginSuccessYn("login_success_yn-sample");
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("created_by-sample");
        return item;
    }

    private MemberLoginHistoryRes.Name sampleName() {
        MemberLoginHistoryRes.Name name = new MemberLoginHistoryRes.Name();
        // TODO: name 필드에 맞게 필요시 값 세팅
        return name;
    }

    private MemberLoginHistoryReq.Filter sampleFilter() {
        MemberLoginHistoryReq.Filter.FilterBuilder builder = MemberLoginHistoryReq.Filter.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.loginAt(LocalDateTime.now());
        builder.loginHistoryUserAgent("login_history_user_agent-sample");
        builder.loginHistoryIp("login_history_ip-sample");
        builder.loginSuccessYn("login_success_yn-sample");
        builder.createdAt(LocalDateTime.now());
        builder.createdBy("created_by-sample");
        return builder.build();
    }

    private MemberLoginHistoryReq.Create sampleCreate() {
        MemberLoginHistoryReq.Create.CreateBuilder builder = MemberLoginHistoryReq.Create.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.loginAt(LocalDateTime.now());
        builder.loginHistoryUserAgent("login_history_user_agent-sample");
        builder.loginHistoryIp("login_history_ip-sample");
        builder.loginSuccessYn("login_success_yn-sample");
        return builder.build();
    }

    private MemberLoginHistoryReq.Update sampleUpdate() {
        MemberLoginHistoryReq.Update.UpdateBuilder builder = MemberLoginHistoryReq.Update.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.loginAt(LocalDateTime.now());
        builder.loginHistoryUserAgent("login_history_user_agent-sample");
        builder.loginHistoryIp("login_history_ip-sample");
        builder.loginSuccessYn("login_success_yn-sample");
        return builder.build();
    }

}
