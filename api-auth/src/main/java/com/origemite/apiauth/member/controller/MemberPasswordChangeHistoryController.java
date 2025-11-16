package com.origemite.apiauth.member.controller;

import com.origemite.apiauth.member.facade.MemberPasswordChangeHistoryFacade;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.web.CommonResponse;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryReq;
import com.origemite.lib.model.auth.dto.MemberPasswordChangeHistoryRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "member-password-changes-histories", description = "MemberPasswordChangeHistory")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-password-changes-histories")
public class MemberPasswordChangeHistoryController {

    private final MemberPasswordChangeHistoryFacade memberPasswordChangeHistoryFacade;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberPasswordChangeHistoryRes.Item>> search(@ModelAttribute @Valid MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberPasswordChangeHistoryRes.Id> save(@RequestBody @Valid MemberPasswordChangeHistoryReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberPasswordChangeHistoryRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberPasswordChangeHistoryRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id, @RequestBody MemberPasswordChangeHistoryReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        memberPasswordChangeHistoryFacade.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberPasswordChangeHistoryRes.Item>> findAll(@ModelAttribute MemberPasswordChangeHistoryReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberPasswordChangeHistoryRes.Item>> findByIds(@RequestParam @Valid List<Integer> ids) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberPasswordChangeHistoryRes.Name>> searchName(@ModelAttribute @Valid MemberPasswordChangeHistoryReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberPasswordChangeHistoryRes.Name>> findAllName(@ModelAttribute @Valid MemberPasswordChangeHistoryReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberPasswordChangeHistoryFacade.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<Integer> ids) {
        memberPasswordChangeHistoryFacade.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}