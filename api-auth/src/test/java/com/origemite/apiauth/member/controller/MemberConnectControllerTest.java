package com.origemite.apiauth.member.controller;

import com.origemite.lib.model.auth.dto.MemberConnectReq;
import com.origemite.lib.model.auth.dto.MemberConnectRes;
import com.origemite.lib.model.auth.service.MemberConnectService;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MemberConnectControllerTest {

    @InjectMocks
    MemberConnectController memberConnectController;

    @Mock
    MemberConnectService memberConnectService;

    @Test
    @DisplayName("MemberConnect 목록 조회 성공")
    void search_success() {
        // given
        MemberConnectReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberConnectRes.Item> page = new PageImpl<>(List.of(sampleItem()));
        given(memberConnectService.search(any(), any())).willReturn(page);

        // when
        var result = memberConnectController.search(filter, pageable);

        // then
        assertNotNull(result);
        verify(memberConnectService).search(any(), any());
    }

    @Test
    @DisplayName("MemberConnect 생성 성공")
    void save_success() {
        // given
        MemberConnectReq.Create create = sampleCreate();
        MemberConnectRes.Id idRes = sampleIdRes();
        given(memberConnectService.save(any())).willReturn(idRes);

        // when
        var result = memberConnectController.save(create);

        // then
        assertNotNull(result);
        verify(memberConnectService).save(any());
    }

    @Test
    @DisplayName("MemberConnect 단건 조회 성공")
    void findById_success() {
        // given
        MemberConnectRes.Item item = sampleItem();
        given(memberConnectService.findById(any())).willReturn(item);
        Integer id = sampleId();

        // when
        var result = memberConnectController.findById(id);

        // then
        assertNotNull(result);
        verify(memberConnectService).findById(any());
    }

    @Test
    @DisplayName("MemberConnect 수정 성공")
    void saveById_success() {
        // given
        MemberConnectReq.Update update = sampleUpdate();
        MemberConnectRes.Id idRes = sampleIdRes();
        Integer id = sampleId();
        given(memberConnectService.saveById(any(), any())).willReturn(idRes);

        // when
        var result = memberConnectController.saveById(id, update);

        // then
        assertNotNull(result);
        verify(memberConnectService).saveById(any(), any());
    }

    @Test
    @DisplayName("MemberConnect 삭제 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();

        // when
        memberConnectController.deleteById(id);

        // then
        verify(memberConnectService).deleteById(any());
    }

    @Test
    @DisplayName("MemberConnect 전체 조회 성공")
    void findAll_success() {
        // given
        MemberConnectReq.Filter filter = sampleFilter();
        given(memberConnectService.findAll(any()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberConnectController.findAll(filter);

        // then
        assertNotNull(result);
        verify(memberConnectService).findAll(any());
    }

    @Test
    @DisplayName("MemberConnect ID 리스트 조회 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        given(memberConnectService.findByIds(anyList()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberConnectController.findByIds(ids);

        // then
        assertNotNull(result);
        verify(memberConnectService).findByIds(anyList());
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberConnectRes.Id sampleIdRes() {
        MemberConnectRes.Id id = new MemberConnectRes.Id();
        id.setId(sampleId());
        return id;
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

}
