package com.origemite.apiauth.member.controller;

import com.origemite.lib.model.auth.dto.MemberOutReq;
import com.origemite.lib.model.auth.dto.MemberOutRes;
import com.origemite.lib.model.auth.service.MemberOutService;
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
class MemberOutControllerTest {

    @InjectMocks
    MemberOutController memberOutController;

    @Mock
    MemberOutService memberOutService;

    @Test
    @DisplayName("MemberOut 목록 조회 성공")
    void search_success() {
        // given
        MemberOutReq.Filter filter = sampleFilter();
        Pageable pageable = PageRequest.of(0, 10);
        Page<MemberOutRes.Item> page = new PageImpl<>(List.of(sampleItem()));
        given(memberOutService.search(any(), any())).willReturn(page);

        // when
        var result = memberOutController.search(filter, pageable);

        // then
        assertNotNull(result);
        verify(memberOutService).search(any(), any());
    }

    @Test
    @DisplayName("MemberOut 생성 성공")
    void save_success() {
        // given
        MemberOutReq.Create create = sampleCreate();
        MemberOutRes.Id idRes = sampleIdRes();
        given(memberOutService.save(any())).willReturn(idRes);

        // when
        var result = memberOutController.save(create);

        // then
        assertNotNull(result);
        verify(memberOutService).save(any());
    }

    @Test
    @DisplayName("MemberOut 단건 조회 성공")
    void findById_success() {
        // given
        MemberOutRes.Item item = sampleItem();
        given(memberOutService.findById(any())).willReturn(item);
        Integer id = sampleId();

        // when
        var result = memberOutController.findById(id);

        // then
        assertNotNull(result);
        verify(memberOutService).findById(any());
    }

    @Test
    @DisplayName("MemberOut 수정 성공")
    void saveById_success() {
        // given
        MemberOutReq.Update update = sampleUpdate();
        MemberOutRes.Id idRes = sampleIdRes();
        Integer id = sampleId();
        given(memberOutService.saveById(any(), any())).willReturn(idRes);

        // when
        var result = memberOutController.saveById(id, update);

        // then
        assertNotNull(result);
        verify(memberOutService).saveById(any(), any());
    }

    @Test
    @DisplayName("MemberOut 삭제 성공")
    void deleteById_success() {
        // given
        Integer id = sampleId();

        // when
        memberOutController.deleteById(id);

        // then
        verify(memberOutService).deleteById(any());
    }

    @Test
    @DisplayName("MemberOut 전체 조회 성공")
    void findAll_success() {
        // given
        MemberOutReq.Filter filter = sampleFilter();
        given(memberOutService.findAll(any()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberOutController.findAll(filter);

        // then
        assertNotNull(result);
        verify(memberOutService).findAll(any());
    }

    @Test
    @DisplayName("MemberOut ID 리스트 조회 성공")
    void findByIds_success() {
        // given
        List<Integer> ids = List.of(sampleId());
        given(memberOutService.findByIds(anyList()))
                .willReturn(List.of(sampleItem()));

        // when
        var result = memberOutController.findByIds(ids);

        // then
        assertNotNull(result);
        verify(memberOutService).findByIds(anyList());
    }

    private Integer sampleId() {
        return 1;
    }

    private MemberOutRes.Id sampleIdRes() {
        MemberOutRes.Id id = new MemberOutRes.Id();
        id.setId(sampleId());
        return id;
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

}
