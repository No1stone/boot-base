package com.origemite.apiauth.member.controller;

import com.origemite.apiauth.member.facade.MemberConnectFacade;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.web.CommonResponse;
import com.origemite.lib.model.auth.dto.MemberConnectReq;
import com.origemite.lib.model.auth.dto.MemberConnectRes;
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

@Tag(name = "member-connects", description = "MemberConnect")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-connects")
public class MemberConnectController {

    private final MemberConnectFacade memberConnectFacade;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberConnectRes.Item>> search(@ModelAttribute @Valid MemberConnectReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberConnectRes.Id> save(@RequestBody @Valid MemberConnectReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberConnectRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberConnectRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id, @RequestBody MemberConnectReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        memberConnectFacade.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberConnectRes.Item>> findAll(@ModelAttribute MemberConnectReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberConnectRes.Item>> findByIds(@RequestParam @Valid List<Integer> ids) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberConnectRes.Name>> searchName(@ModelAttribute @Valid MemberConnectReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberConnectRes.Name>> findAllName(@ModelAttribute @Valid MemberConnectReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberConnectFacade.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<Integer> ids) {
        memberConnectFacade.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}