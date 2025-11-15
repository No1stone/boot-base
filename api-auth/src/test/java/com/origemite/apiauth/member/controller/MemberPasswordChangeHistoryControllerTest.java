package com.origemite.apiauth.member.controller;

import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryRes;
import com.origemite.lib.model.auth.service.MemberPasswordChangeHistoryService;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;


@ExtendWith(MockitoExtension.class)
class MemberPasswordChangeHistoryControllerTest {

    @InjectMocks
    MemberPasswordChangeHistoryController memberPasswordChangeHistoryController;

    @Mock
    MemberPasswordChangeHistoryService memberPasswordChangeHistoryService;

    @Test
    @DisplayName("MemberPasswordChangeHistory 목록 조회 성공")
    void search_success() {
        // given
        MemberPasswordChangeHistoryReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberPasswordChangeHistoryRes.Item> page = new PageImpl<>(List.of(sampleItem()));
        given(memberPasswordChangeHistoryService.search(any(), any())).willReturn(page);

        // when
        var result = memberPasswordChangeHistoryController.search(filter, pageable);

        // then
        assertNotNull(result);
        verify(memberPasswordChangeHistoryService).search(any(), any());
    }

    @Test
    @DisplayName("MemberPasswordChangeHistory 생성 성공")
    void save_success() {
        // given
        MemberPasswordChangeHistoryReq.Create create = sampleCreate();
        MemberPasswordChangeHistoryRes.Id idRes = sampleIdRes();
        given(memberPasswordChangeHistoryService.save(any())).willReturn(idRes);

        // when
        var result = memberPasswordChangeHistoryController.save(create);

        // then
        assertNotNull(result);
        verify(memberPasswordChangeHistoryService).save(any());
    }

    @Test
    @DisplayName("MemberPasswordChangeHistory 단건 조회 성공")
    void findById_success() {
        // given
        MemberPasswordChangeHistoryRes.Item item = sampleItem();
        given(memberPasswordChangeHistoryService.findById(any())).willReturn(item);
        Integer id = sampleId();

        // when
        var result = memberPasswordChangeHistoryController.findById(id);

        // then
        assertNotNull(result);
        verify(memberPasswordChangeHistoryService).findById(any());
    }

    @Test
    @DisplayName("MemberPasswordChangeHistory 수정 성공")
    void saveById_success() {
        // given
        MemberPasswordChangeHistoryReq.Update update = sampleUpdate();
        MemberPasswordChangeHistoryRes.Id idRes = sampleIdRes();
        Integer id = sampleId();
        given(memberPasswordChangeHistoryService.saveById(any(), any())).willReturn(idRes);

        // when
        var result = memberPasswordChangeHistoryController.saveById(id, update);

        // then
        assertNotNull(result);
        verify(memberPasswordChangeHistoryService).saveById(any(), any());
    }

    @Test
    @DisplayName("MemberPasswordChangeHistory 삭제 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();

        // when
        memberPasswordChangeHistoryController.deleteById(id);

        // then
        verify(memberPasswordChangeHistoryService).deleteById(any());
    }

    @Test
    @DisplayName("MemberPasswordChangeHistory 전체 조회 성공")
    void findAll_success() {
        // given
        MemberPasswordChangeHistoryReq.Filter filter = sampleFilter();
        given(memberPasswordChangeHistoryService.findAll(any()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberPasswordChangeHistoryController.findAll(filter);

        // then
        assertNotNull(result);
        verify(memberPasswordChangeHistoryService).findAll(any());
    }

    @Test
    @DisplayName("MemberPasswordChangeHistory ID 리스트 조회 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        given(memberPasswordChangeHistoryService.findByIds(anyList()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberPasswordChangeHistoryController.findByIds(ids);

        // then
        assertNotNull(result);
        verify(memberPasswordChangeHistoryService).findByIds(anyList());
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberPasswordChangeHistoryRes.Id sampleIdRes() {
        MemberPasswordChangeHistoryRes.Id id = new MemberPasswordChangeHistoryRes.Id();
        id.setId(sampleId());
        return id;
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

}
