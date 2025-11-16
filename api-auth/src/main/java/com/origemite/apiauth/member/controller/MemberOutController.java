package com.origemite.apiauth.member.controller;

import com.origemite.apiauth.member.facade.MemberOutFacade;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.web.CommonResponse;
import com.origemite.lib.model.auth.dto.MemberOutReq;
import com.origemite.lib.model.auth.dto.MemberOutRes;
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

@Tag(name = "member-out", description = "MemberOut")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-out")
public class MemberOutController {

    private final MemberOutFacade memberOutFacade;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberOutRes.Item>> search(@ModelAttribute @Valid MemberOutReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberOutRes.Id> save(@RequestBody @Valid MemberOutReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberOutRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberOutRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id, @RequestBody MemberOutReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        memberOutFacade.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberOutRes.Item>> findAll(@ModelAttribute MemberOutReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberOutRes.Item>> findByIds(@RequestParam @Valid List<Integer> ids) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberOutRes.Name>> searchName(@ModelAttribute @Valid MemberOutReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberOutRes.Name>> findAllName(@ModelAttribute @Valid MemberOutReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberOutFacade.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<Integer> ids) {
        memberOutFacade.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}