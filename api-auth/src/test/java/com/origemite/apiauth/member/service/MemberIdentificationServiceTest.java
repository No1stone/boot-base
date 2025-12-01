package com.origemite.apiauth.member.service;

import com.origemite.lib.common.util.ModelMapperUtil;
import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.entity.MemberIdentification;
import com.origemite.lib.model.auth.repository.MemberIdentificationRepository;
import com.origemite.lib.model.auth.service.MemberIdentificationService;
import com.origemite.lib.model.enums.common.EnMobileCarrierCode;
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
class MemberIdentificationServiceTest {

    @InjectMocks
    MemberIdentificationService memberIdentificationService;

    @Mock
    MemberIdentificationRepository memberIdentificationRepository;

    @Test
    @DisplayName("MemberIdentificationService - search 성공")
    void search_success() {
        // given
        MemberIdentificationReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberIdentification entity = sampleEntity();
        Page<MemberIdentification> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberIdentificationRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberIdentification.class))
                  .thenReturn(entity);
            given(memberIdentificationRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberIdentificationRes.Item.class))
                  .thenReturn(itemPage);

            // when
            Page<MemberIdentificationRes.Item> result = memberIdentificationService.search(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberIdentificationRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - save 성공")
    void save_success() {
        // given
        MemberIdentificationReq.Create create = sampleCreate();
        MemberIdentification entity = sampleEntity();
        MemberIdentificationRes.Id idRes = sampleIdRes();

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(create, MemberIdentification.class))
                  .thenReturn(entity);
            given(memberIdentificationRepository.save(any(MemberIdentification.class)))
                    .willReturn(entity);
            mocked.when(() -> ModelMapperUtil.map(entity, MemberIdentificationRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberIdentificationRes.Id result = memberIdentificationService.save(create);

            // then
            assertNotNull(result);
            verify(memberIdentificationRepository).save(any(MemberIdentification.class));
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - findById 성공")
    void findById_success() {
        // given
        Integer id = sampleId();
        MemberIdentification entity = sampleEntity();
        MemberIdentificationRes.Item item = sampleItem();

        given(memberIdentificationRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberIdentificationRes.Item.class))
                  .thenReturn(item);

            // when
            MemberIdentificationRes.Item result = memberIdentificationService.findById(id);

            // then
            assertNotNull(result);
            verify(memberIdentificationRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - saveById 성공")
    void saveById_success() {
        // given
        Integer id = sampleId();
        MemberIdentificationReq.Update update = sampleUpdate();
        MemberIdentification entity = sampleEntity();
        MemberIdentificationRes.Id idRes = sampleIdRes();

        given(memberIdentificationRepository.findById(id)).willReturn(Optional.of(entity));
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(entity, MemberIdentificationRes.Id.class))
                  .thenReturn(idRes);

            // when
            MemberIdentificationRes.Id result = memberIdentificationService.saveById(id, update);

            // then
            assertNotNull(result);
            verify(memberIdentificationRepository).findById(id);
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - deleteById 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();
        MemberIdentification entity = sampleEntity();
        given(memberIdentificationRepository.findById(id)).willReturn(Optional.of(entity));

        // when
        memberIdentificationService.deleteById(id);

        // then
        verify(memberIdentificationRepository).findById(id);
    }

    @Test
    @DisplayName("MemberIdentificationService - findAll 성공")
    void findAll_success() {
        // given
        MemberIdentificationReq.Filter filter = sampleFilter();
        MemberIdentification entity = sampleEntity();
        Page<MemberIdentification> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberIdentificationRes.Item> itemPage = new PageImpl<>(List.of(sampleItem()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberIdentification.class))
                  .thenReturn(entity);
            given(memberIdentificationRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberIdentificationRes.Item.class))
                  .thenReturn(itemPage);

            // when
            var result = memberIdentificationService.findAll(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberIdentificationRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - findByIds 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberIdentification> entities = List.of(sampleEntity());
        List<MemberIdentificationRes.Item> items = List.of(sampleItem());

        given(memberIdentificationRepository.findByIdIn(ids)).willReturn(entities);
        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.mapAll(entities, MemberIdentificationRes.Item.class))
                  .thenReturn(items);

            // when
            var result = memberIdentificationService.findByIds(ids);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberIdentificationRepository).findByIdIn(ids);
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - searchName 성공")
    void searchName_success() {
        // given
        MemberIdentificationReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        MemberIdentification entity = sampleEntity();
        Page<MemberIdentification> entityPage = new PageImpl<>(List.of(entity));
        Page<MemberIdentificationRes.Name> namePage = new PageImpl<>(List.of(sampleName()));

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberIdentification.class))
                  .thenReturn(entity);
            given(memberIdentificationRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage, MemberIdentificationRes.Name.class))
                  .thenReturn(namePage);

            // when
            Page<MemberIdentificationRes.Name> result = memberIdentificationService.searchName(filter, pageable);

            // then
            assertNotNull(result);
            assertEquals(1, result.getContent().size());
            verify(memberIdentificationRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - findAllName 성공")
    void findAllName_success() {
        // given
        MemberIdentificationReq.Filter filter = sampleFilter();
        MemberIdentification entity = sampleEntity();
        Page<MemberIdentification> entityPage = new PageImpl<>(List.of(entity));
        List<MemberIdentificationRes.Name> names = List.of(sampleName());

        try (MockedStatic<ModelMapperUtil> mocked = mockStatic(ModelMapperUtil.class)) {
            mocked.when(() -> ModelMapperUtil.map(filter, MemberIdentification.class))
                  .thenReturn(entity);
            given(memberIdentificationRepository.findAll(any(Example.class), any(Pageable.class)))
                    .willReturn(entityPage);
            mocked.when(() -> ModelMapperUtil.mapAll(entityPage.stream().toList(), MemberIdentificationRes.Name.class))
                  .thenReturn(names);

            // when
            var result = memberIdentificationService.findAllName(filter);

            // then
            assertNotNull(result);
            assertEquals(1, result.size());
            verify(memberIdentificationRepository).findAll(any(Example.class), any(Pageable.class));
        }
    }

    @Test
    @DisplayName("MemberIdentificationService - deleteByIds 성공")
    void deleteByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        List<MemberIdentification> entities = List.of(sampleEntity());
        given(memberIdentificationRepository.findByIdIn(ids)).willReturn(entities);

        // when
        memberIdentificationService.deleteByIds(ids);

        // then
        verify(memberIdentificationRepository).findByIdIn(ids);
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberIdentification sampleEntity() {
        MemberIdentification entity = MemberIdentification.builder().build();
        entity.setId(sampleId());
        entity.setName("name-sample");
        entity.setMobilePhoneNumber("mobile_phone_number-sample");
        entity.setEmail("email-sample");
        entity.setCi("ci-sample");
        entity.setBirthday("birthday-sample");
        entity.setGender("gender-sample");
        entity.setForeignYn("foreign_yn-sample");
        entity.setNameSha("name_sha-sample");
        entity.setMobilePhoneNumberSha("mobile_phone_number_sha-sample");
        entity.setEmailSha("email_sha-sample");
        entity.setCiSha("ci_sha-sample");
        entity.setCipherKeyId("cipher_key_id-sample");
        entity.setMobileCarrierCode(EnMobileCarrierCode.SKT);
        entity.setMemberId("member_id-sample");
        entity.setStatus("status-sample");
        return entity;
    }

    private MemberIdentificationRes.Id sampleIdRes() {
        MemberIdentificationRes.Id id = new MemberIdentificationRes.Id();
        id.setId(sampleId());
        return id;
    }

    private MemberIdentificationRes.Item sampleItem() {
        MemberIdentificationRes.Item item = new MemberIdentificationRes.Item();
        item.setId(sampleId());
        item.setName("name-sample");
        item.setMobilePhoneNumber("mobile_phone_number-sample");
        item.setEmail("email-sample");
        item.setCi("ci-sample");
        item.setBirthday("birthday-sample");
        item.setGender("gender-sample");
        item.setForeignYn("foreign_yn-sample");
        item.setNameSha("name_sha-sample");
        item.setMobilePhoneNumberSha("mobile_phone_number_sha-sample");
        item.setEmailSha("email_sha-sample");
        item.setCiSha("ci_sha-sample");
        item.setCipherKeyId("cipher_key_id-sample");
        item.setMobileCarrierCode("mobile_carrier_code-sample");
        item.setMemberId("member_id-sample");
        item.setStatus("status-sample");
        item.setUpdatedAt(LocalDateTime.now());
        item.setUpdatedBy("updated_by-sample");
        item.setVersion(1);
        item.setCreatedAt(LocalDateTime.now());
        item.setCreatedBy("created_by-sample");
        return item;
    }

    private MemberIdentificationRes.Name sampleName() {
        MemberIdentificationRes.Name name = new MemberIdentificationRes.Name();
        // TODO: name 필드에 맞게 필요시 값 세팅
        return name;
    }

    private MemberIdentificationReq.Filter sampleFilter() {
        MemberIdentificationReq.Filter.FilterBuilder builder = MemberIdentificationReq.Filter.builder();
        builder.id(sampleId());
        builder.name("name-sample");
        builder.mobilePhoneNumber("mobile_phone_number-sample");
        builder.email("email-sample");
        builder.ci("ci-sample");
        builder.birthday("birthday-sample");
        builder.gender("gender-sample");
        builder.foreignYn("foreign_yn-sample");
        builder.nameSha("name_sha-sample");
        builder.mobilePhoneNumberSha("mobile_phone_number_sha-sample");
        builder.emailSha("email_sha-sample");
        builder.ciSha("ci_sha-sample");
        builder.cipherKeyId("cipher_key_id-sample");
        builder.mobileCarrierCode("mobile_carrier_code-sample");
        builder.memberId("member_id-sample");
        builder.status("status-sample");
        builder.updatedAt(LocalDateTime.now());
        builder.updatedBy("updated_by-sample");
        builder.version(1);
        builder.createdAt(LocalDateTime.now());
        builder.createdBy("created_by-sample");
        return builder.build();
    }

    private MemberIdentificationReq.Create sampleCreate() {
        MemberIdentificationReq.Create.CreateBuilder builder = MemberIdentificationReq.Create.builder();
        builder.id(sampleId());
        builder.name("name-sample");
        builder.mobilePhoneNumber("mobile_phone_number-sample");
        builder.email("email-sample");
        builder.ci("ci-sample");
        builder.birthday("birthday-sample");
        builder.gender("gender-sample");
        builder.foreignYn("foreign_yn-sample");
        builder.nameSha("name_sha-sample");
        builder.mobilePhoneNumberSha("mobile_phone_number_sha-sample");
        builder.emailSha("email_sha-sample");
        builder.ciSha("ci_sha-sample");
        builder.cipherKeyId("cipher_key_id-sample");
        builder.mobileCarrierCode("mobile_carrier_code-sample");
        builder.memberId("member_id-sample");
        builder.status("status-sample");
        return builder.build();
    }

    private MemberIdentificationReq.Update sampleUpdate() {
        MemberIdentificationReq.Update.UpdateBuilder builder = MemberIdentificationReq.Update.builder();
        builder.id(sampleId());
        builder.name("name-sample");
        builder.mobilePhoneNumber("mobile_phone_number-sample");
        builder.email("email-sample");
        builder.ci("ci-sample");
        builder.birthday("birthday-sample");
        builder.gender("gender-sample");
        builder.foreignYn("foreign_yn-sample");
        builder.nameSha("name_sha-sample");
        builder.mobilePhoneNumberSha("mobile_phone_number_sha-sample");
        builder.emailSha("email_sha-sample");
        builder.ciSha("ci_sha-sample");
        builder.cipherKeyId("cipher_key_id-sample");
        builder.mobileCarrierCode("mobile_carrier_code-sample");
        builder.memberId("member_id-sample");
        builder.status("status-sample");
        return builder.build();
    }

}
