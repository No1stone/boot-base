package com.origemite.apiauth.member.controller;

import com.origemite.apiauth.member.facade.MemberIdentificationBffFacade;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.web.CommonResponse;
import com.origemite.lib.model.member.dto.MemberIdentificationReq;
import com.origemite.lib.model.member.dto.MemberIdentificationRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Tag(name = "0ba9ade2-dc55-4dd5-be07-d3d176a45607", description = "MemberIdentification")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member-dentification")
public class MemberIdentificationController {

    private final MemberIdentificationBffFacade memberIdentificationBffFacade;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberIdentificationRes.Item>> search(@ModelAttribute @Valid MemberIdentificationReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberIdentificationRes.Id> save(@RequestBody @Valid MemberIdentificationReq.CreateForEncrypt create) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.save(create));
    }


    @Operation(summary = "생성", hidden = true)
    @PostMapping("/hidden")
    public CommonResponse<MemberIdentificationRes.Id> save(@RequestBody @Valid MemberIdentificationReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberIdentificationRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberIdentificationRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id, @RequestBody MemberIdentificationReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        memberIdentificationBffFacade.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberIdentificationRes.Item>> findAll(@ModelAttribute MemberIdentificationReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberIdentificationRes.Item>> findByIds(@RequestParam @Valid List<Integer> ids) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberIdentificationRes.Name>> searchName(@ModelAttribute @Valid MemberIdentificationReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberIdentificationRes.Name>> findAllName(@ModelAttribute @Valid MemberIdentificationReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberIdentificationBffFacade.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<Integer> ids) {
        memberIdentificationBffFacade.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}