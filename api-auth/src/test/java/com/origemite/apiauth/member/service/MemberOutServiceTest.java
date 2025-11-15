package com.origemite.apiauth.member.service;

import com.origemite.lib.common.exception.BizErrorException;
import com.origemite.lib.common.util.ModelMapperUtil;
import com.origemite.lib.model.auth.dto.MemberOutReq;
import com.origemite.lib.model.auth.dto.MemberOutRes;
import com.origemite.lib.model.auth.entity.MemberOut;
import com.origemite.lib.model.auth.repository.MemberOutRepository;
import com.origemite.lib.model.auth.service.MemberOutService;
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
class MemberOutServiceTest {

    @InjectMocks
    MemberOutService memberOutService;

    @Mock
    MemberOutRepository memberOutRepository;

    @Test
    @DisplayName("MemberOutService - search 성공")
    void search_success() {
        // given
        MemberOutReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberOut entity = sampleEntity();
        Page<MemberOut> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberOutRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberOut.class))
                  .thenReturn(entity);
            given(memberOutRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberOutRes.Item.class))
                  .thenReturn(itemPage);

            // when
            Page<MemberOutRes.Item> result = memberOutService.search(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberOutRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberOutService - save 성공")
    void save_success() {
        // given
        MemberOutReq.Create create = sampleCreate();
        MemberOut entity = sampleEntity();
        MemberOutRes.Id idRes = sampleIdRes();

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(create, MemberOut.class))
                  .thenReturn(entity);
            given(memberOutRepository.save(any(MemberOut.class)))
                    .willReturn(entity);
            mocked.when(() -> ModelMapperUtil.map(entity, MemberOutRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberOutRes.Id result = memberOutService.save(create);

            // then
            assertNotNull(result);
            verify(memberOutRepository).save(any(MemberOut.class));
        }
    }

    @Test
    @DisplayName("MemberOutService - findById 성공")
    void findById_success() {
        // given
        Integer id = sampleId();
        MemberOut entity = sampleEntity();
        MemberOutRes.Item item = sampleItem();

        given(memberOutRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberOutRes.Item.class))
                  .thenReturn(item);

            // when
            MemberOutRes.Item result = memberOutService.findById(id);

            // then
            assertNotNull(result);
            verify(memberOutRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberOutService - saveById 성공")
    void saveById_success() {
        // given
        Integer id = sampleId();
        MemberOutReq.Update update = sampleUpdate();
        MemberOut entity = sampleEntity();
        MemberOutRes.Id idRes = sampleIdRes();

        given(memberOutRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberOutRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberOutRes.Id result = memberOutService.saveById(id, update);

            // then
            assertNotNull(result);
            verify(memberOutRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberOutService - deleteById 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();
        MemberOut entity = sampleEntity();
        given(memberOutRepository.findById(id)).willReturn(Optional.of(entity));

        // when
        memberOutService.deleteById(id);

        // then
        verify(memberOutRepository).findById(id);
    }

    @Test
    @DisplayName("MemberOutService - findAll 성공")
    void findAll_success() {
        // given
        MemberOutReq.Filter filter = sampleFilter();
        MemberOut entity = sampleEntity();
        Page<MemberOut> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberOutRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberOut.class))
                  .thenReturn(entity);
            given(memberOutRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberOutRes.Item.class))
                  .thenReturn(itemPage);

            // when
            var result = memberOutService.findAll(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberOutRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberOutService - findByIds 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberOut> entities = List.of(sampleEntity());
        List<MemberOutRes.Item> items = List.of(sampleItem());

        given(memberOutRepository.findByIdIn(ids)).willReturn(entities);
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.mapAll(entities, MemberOutRes.Item.class))
                  .thenReturn(items);

            // when
            var result = memberOutService.findByIds(ids);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberOutRepository).findByIdIn(ids);
        }
    }

    @Test
    @DisplayName("MemberOutService - searchName 성공")
    void searchName_success() {
        // given
        MemberOutReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberOut entity = sampleEntity();
        Page<MemberOut> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberOutRes.Name> namePage = new PageImpl<>(List.of(sampleName()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberOut.class))
                  .thenReturn(entity);
            given(memberOutRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberOutRes.Name.class))
                  .thenReturn(namePage);

            // when
            Page<MemberOutRes.Name> result = memberOutService.searchName(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberOutRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberOutService - findAllName 성공")
    void findAllName_success() {
        // given
        MemberOutReq.Filter filter = sampleFilter();
        MemberOut entity = sampleEntity();
        Page<MemberOut> entityPage = new PageImpl<>(List.of(entity));
        List<MemberOutRes.Name> names = List.of(sampleName());

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberOut.class))
                  .thenReturn(entity);
            given(memberOutRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage.stream().toList(), MemberOutRes.Name.class))
                  .thenReturn(names);

            // when
            var result = memberOutService.findAllName(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberOutRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberOutService - deleteByIds 성공")
    void deleteByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberOut> entities = List.of(sampleEntity());
        given(memberOutRepository.findByIdIn(ids)).willReturn(entities);

        // when
        memberOutService.deleteByIds(ids);

        // then
        verify(memberOutRepository).findByIdIn(ids);
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberOut sampleEntity() {
        MemberOut entity = MemberOut.builder().build();
        entity.setId(sampleId());
        entity.setMemberId("member_id-sample");
        entity.setOutAt(LocalDateTime.now());
        entity.setOutReason("out_reason-sample");
        return entity;
    }

    private MemberOutRes.Id sampleIdRes() {
        MemberOutRes.Id id = new MemberOutRes.Id();
        id.setId(sampleId());
        return id;
    }

    private MemberOutRes.Item sampleItem() {
        MemberOutRes.Item item = new MemberOutRes.Item();
        item.setId(sampleId());
        item.setMemberId("member_id-sample");
        item.setOutAt(LocalDateTime.now());
        item.setOutReason("out_reason-sample");
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy("updated_by-sample");
        item.setVersion(1);
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("created_by-sample");
        return item;
    }

    private MemberOutRes.Name sampleName() {
        MemberOutRes.Name name = new MemberOutRes.Name();
        // TODO: name 필드에 맞게 필요시 값 세팅
        return name;
    }

    private MemberOutReq.Filter sampleFilter() {
        MemberOutReq.Filter.FilterBuilder builder = MemberOutReq.Filter.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.outAt(LocalDateTime.now());
        builder.outReason("out_reason-sample");
        builder.updatedAt(LocalDateTime.now());
        builder.updatedBy("updated_by-sample");
        builder.version(1);
        builder.createdAt(LocalDateTime.now());
        builder.createdBy("created_by-sample");
        return builder.build();
    }

    private MemberOutReq.Create sampleCreate() {
        MemberOutReq.Create.CreateBuilder builder = MemberOutReq.Create.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.outAt(LocalDateTime.now());
        builder.outReason("out_reason-sample");
        return builder.build();
    }

    private MemberOutReq.Update sampleUpdate() {
        MemberOutReq.Update.UpdateBuilder builder = MemberOutReq.Update.builder();
        builder.id(sampleId());
        builder.memberId("member_id-sample");
        builder.outAt(LocalDateTime.now());
        builder.outReason("out_reason-sample");
        return builder.build();
    }

}
