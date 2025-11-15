package com.origemite.apiauth.member.controller;

import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryRes;
import com.origemite.lib.model.auth.service.MemberLoginHistoryService;
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
class MemberLoginHistoryControllerTest {

    @InjectMocks
    MemberLoginHistoryController memberLoginHistoryController;

    @Mock
    MemberLoginHistoryService memberLoginHistoryService;

    @Test
    @DisplayName("MemberLoginHistory 목록 조회 성공")
    void search_success() {
        // given
        MemberLoginHistoryReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberLoginHistoryRes.Item> page = new PageImpl<>(List.of(sampleItem()));
        given(memberLoginHistoryService.search(any(), any())).willReturn(page);

        // when
        var result = memberLoginHistoryController.search(filter, pageable);

        // then
        assertNotNull(result);
        verify(memberLoginHistoryService).search(any(), any());
    }

    @Test
    @DisplayName("MemberLoginHistory 생성 성공")
    void save_success() {
        // given
        MemberLoginHistoryReq.Create create = sampleCreate();
        MemberLoginHistoryRes.Id idRes = sampleIdRes();
        given(memberLoginHistoryService.save(any())).willReturn(idRes);

        // when
        var result = memberLoginHistoryController.save(create);

        // then
        assertNotNull(result);
        verify(memberLoginHistoryService).save(any());
    }

    @Test
    @DisplayName("MemberLoginHistory 단건 조회 성공")
    void findById_success() {
        // given
        MemberLoginHistoryRes.Item item = sampleItem();
        given(memberLoginHistoryService.findById(any())).willReturn(item);
        Integer id = sampleId();

        // when
        var result = memberLoginHistoryController.findById(id);

        // then
        assertNotNull(result);
        verify(memberLoginHistoryService).findById(any());
    }

    @Test
    @DisplayName("MemberLoginHistory 수정 성공")
    void saveById_success() {
        // given
        MemberLoginHistoryReq.Update update = sampleUpdate();
        MemberLoginHistoryRes.Id idRes = sampleIdRes();
        Integer id = sampleId();
        given(memberLoginHistoryService.saveById(any(), any())).willReturn(idRes);

        // when
        var result = memberLoginHistoryController.saveById(id, update);

        // then
        assertNotNull(result);
        verify(memberLoginHistoryService).saveById(any(), any());
    }

    @Test
    @DisplayName("MemberLoginHistory 삭제 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();

        // when
        memberLoginHistoryController.deleteById(id);

        // then
        verify(memberLoginHistoryService).deleteById(any());
    }

    @Test
    @DisplayName("MemberLoginHistory 전체 조회 성공")
    void findAll_success() {
        // given
        MemberLoginHistoryReq.Filter filter = sampleFilter();
        given(memberLoginHistoryService.findAll(any()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberLoginHistoryController.findAll(filter);

        // then
        assertNotNull(result);
        verify(memberLoginHistoryService).findAll(any());
    }

    @Test
    @DisplayName("MemberLoginHistory ID 리스트 조회 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        given(memberLoginHistoryService.findByIds(anyList()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberLoginHistoryController.findByIds(ids);

        // then
        assertNotNull(result);
        verify(memberLoginHistoryService).findByIds(anyList());
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberLoginHistoryRes.Id sampleIdRes() {
        MemberLoginHistoryRes.Id id = new MemberLoginHistoryRes.Id();
        id.setId(sampleId());
        return id;
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

}
