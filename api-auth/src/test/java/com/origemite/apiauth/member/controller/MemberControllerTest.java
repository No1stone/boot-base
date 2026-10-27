package com.origemite.apiauth.member.controller;

import com.origemite.lib.model.member.dto.MemberReq;
import com.origemite.lib.model.member.dto.MemberRes;
import com.origemite.apiauth.member.facade.MemberBffFacade;
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
class MemberControllerTest {

    @InjectMocks
    MemberController memberController;

    @Mock
    MemberBffFacade memberBffFacade;

    @Test
    @DisplayName("Member 목록 조회 성공")
    void search_success() {
        // given
        MemberReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberRes.Item> page = new PageImpl<>(List.of(sampleItem()));
        given(memberBffFacade.search(any(), any())).willReturn(page);

        // when
        var result = memberController.search(filter, pageable);

        // then
        assertNotNull(result);
        verify(memberBffFacade).search(any(), any());
    }

    @Test
    @DisplayName("Member 생성 성공")
    void save_success() {
        // given
        MemberReq.Create create = sampleCreate();
        MemberRes.Id idRes = sampleIdRes();
        given(memberBffFacade.save(any())).willReturn(idRes);

        // when
        var result = memberController.save(create);

        // then
        assertNotNull(result);
        verify(memberBffFacade).save(any());
    }

    @Test
    @DisplayName("Member 단건 조회 성공")
    void findById_success() {
        // given
        MemberRes.Item item = sampleItem();
        given(memberBffFacade.findById(any())).willReturn(item);
        String id = sampleId();

        // when
        var result = memberController.findById(id);

        // then
        assertNotNull(result);
        verify(memberBffFacade).findById(any());
    }

    @Test
    @DisplayName("Member 수정 성공")
    void saveById_success() {
        // given
        MemberReq.Update update = sampleUpdate();
        MemberRes.Id idRes = sampleIdRes();
        String id = sampleId();
        given(memberBffFacade.saveById(any(), any())).willReturn(idRes);

        // when
        var result = memberController.saveById(id, update);

        // then
        assertNotNull(result);
        verify(memberBffFacade).saveById(any(), any());
    }

    @Test
    @DisplayName("Member 삭제 성공")
    void deleteById_success() {
        // given
        String id = sampleId();

        // when
        memberController.deleteById(id);

        // then
        verify(memberBffFacade).deleteById(any());
    }

    @Test
    @DisplayName("Member 전체 조회 성공")
    void findAll_success() {
        // given
        MemberReq.Filter filter = sampleFilter();
        given(memberBffFacade.findAll(any()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberController.findAll(filter);

        // then
        assertNotNull(result);
        verify(memberBffFacade).findAll(any());
    }

    @Test
    @DisplayName("Member ID 리스트 조회 성공")
    void findByIds_success() {
        // given
        List<String> ids = List.of(sampleId());
        given(memberBffFacade.findByIds(anyList()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberController.findByIds(ids);

        // then
        assertNotNull(result);
        verify(memberBffFacade).findByIds(anyList());
    }

    private String sampleId() {
        return "sample-id";
    }

    private MemberRes.Id sampleIdRes() {
        MemberRes.Id id = new MemberRes.Id();
        id.setId(sampleId());
        return id;
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

}
