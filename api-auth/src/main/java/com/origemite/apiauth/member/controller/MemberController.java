package com.origemite.apiauth.member.controller;

import com.origemite.apiauth.member.facade.MemberFacade;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.web.CommonResponse;
import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
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

@Tag(name = "member", description = "Member")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberFacade memberFacade;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberRes.Item>> search(@ModelAttribute @Valid MemberReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberFacade.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberRes.Id> save(@RequestBody @Valid MemberReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberFacade.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") String id) {
        return CommonResponseUtils.responseSuccess(memberFacade.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") String id, @RequestBody MemberReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberFacade.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") String id) {
        memberFacade.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberRes.Item>> findAll(@ModelAttribute MemberReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberFacade.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberRes.Item>> findByIds(@RequestParam @Valid List<String> ids) {
        return CommonResponseUtils.responseSuccess(memberFacade.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberRes.Name>> searchName(@ModelAttribute @Valid MemberReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberFacade.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberRes.Name>> findAllName(@ModelAttribute @Valid MemberReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberFacade.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<String> ids) {
        memberFacade.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}