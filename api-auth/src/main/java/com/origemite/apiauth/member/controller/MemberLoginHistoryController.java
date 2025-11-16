package com.origemite.apiauth.member.controller;

import com.origemite.apiauth.member.facade.MemberLoginHistoryFacade;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.web.CommonResponse;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryReq;
import com.origemite.lib.model.auth.dto.MemberLoginHistoryRes;
import com.origemite.lib.model.auth.service.MemberLoginHistoryService;
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

@Tag(name = "member-login-histories", description = "MemberLoginHistory")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-login-histories")
public class MemberLoginHistoryController {

    private final MemberLoginHistoryFacade memberLoginHistoryFacade;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberLoginHistoryRes.Item>> search(@ModelAttribute @Valid MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberLoginHistoryRes.Id> save(@RequestBody @Valid MemberLoginHistoryReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberLoginHistoryRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberLoginHistoryRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id, @RequestBody MemberLoginHistoryReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        memberLoginHistoryFacade.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberLoginHistoryRes.Item>> findAll(@ModelAttribute MemberLoginHistoryReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberLoginHistoryRes.Item>> findByIds(@RequestParam @Valid List<Integer> ids) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberLoginHistoryRes.Name>> searchName(@ModelAttribute @Valid MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberLoginHistoryRes.Name>> findAllName(@ModelAttribute @Valid MemberLoginHistoryReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryFacade.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<Integer> ids) {
        memberLoginHistoryFacade.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}