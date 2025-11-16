package com.origemite.apiauth.member.controller;

import com.origemite.lib.model.auth.dto.MemberIdentificationReq;
import com.origemite.lib.model.auth.dto.MemberIdentificationRes;
import com.origemite.lib.model.auth.service.MemberIdentificationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberIdentificationControllerTest {

    @InjectMocks
    MemberIdentificationController memberIdentificationController;

    @Mock
    MemberIdentificationService memberIdentificationService;

    @Test
    @DisplayName("MemberIdentification 목록 조회 성공")
    void search_success() {
        // given
        MemberIdentificationReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberIdentificationRes.Item> page = new PageImpl<>(List.of(sampleItem()));
        given(memberIdentificationService.search(any(), any())).willReturn(page);

        // when
        var result = memberIdentificationController.search(filter, pageable);

        // then
        assertNotNull(result);
        verify(memberIdentificationService).search(any(), any());
    }

    @Test
    @DisplayName("MemberIdentification 생성 성공")
    void save_success() {
        // given
        MemberIdentificationReq.Create create = sampleCreate();
        MemberIdentificationRes.Id idRes = sampleIdRes();
        given(memberIdentificationService.save(any())).willReturn(idRes);

        // when
        var result = memberIdentificationController.save(create);

        // then
        assertNotNull(result);
        verify(memberIdentificationService).save(any());
    }

    @Test
    @DisplayName("MemberIdentification 단건 조회 성공")
    void findById_success() {
        // given
        MemberIdentificationRes.Item item = sampleItem();
        given(memberIdentificationService.findById(any())).willReturn(item);
        Integer id = sampleId();

        // when
        var result = memberIdentificationController.findById(id);

        // then
        assertNotNull(result);
        verify(memberIdentificationService).findById(any());
    }

    @Test
    @DisplayName("MemberIdentification 수정 성공")
    void saveById_success() {
        // given
        MemberIdentificationReq.Update update = sampleUpdate();
        MemberIdentificationRes.Id idRes = sampleIdRes();
        Integer id = sampleId();
        given(memberIdentificationService.saveById(any(), any())).willReturn(idRes);

        // when
        var result = memberIdentificationController.saveById(id, update);

        // then
        assertNotNull(result);
        verify(memberIdentificationService).saveById(any(), any());
    }

    @Test
    @DisplayName("MemberIdentification 삭제 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();

        // when
        memberIdentificationController.deleteById(id);

        // then
        verify(memberIdentificationService).deleteById(any());
    }

    @Test
    @DisplayName("MemberIdentification 전체 조회 성공")
    void findAll_success() {
        // given
        MemberIdentificationReq.Filter filter = sampleFilter();
        given(memberIdentificationService.findAll(any()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberIdentificationController.findAll(filter);

        // then
        assertNotNull(result);
        verify(memberIdentificationService).findAll(any());
    }

    @Test
    @DisplayName("MemberIdentification ID 리스트 조회 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        given(memberIdentificationService.findByIds(anyList()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberIdentificationController.findByIds(ids);

        // then
        assertNotNull(result);
        verify(memberIdentificationService).findByIds(anyList());
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberIdentificationRes.Id sampleIdRes() {
        MemberIdentificationRes.Id id = new MemberIdentificationRes.Id();
        id.setId(sampleId());
        return id;
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

}
