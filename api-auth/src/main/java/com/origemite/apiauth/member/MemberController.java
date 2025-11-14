package com.origemite.apiauth.member;

import com.origemite.lib.model.auth.dto.MemberReq;
import com.origemite.lib.model.auth.dto.MemberRes;
import com.origemite.lib.model.auth.service.MemberService;
import com.origemite.lib.common.util.CommonResponseUtils;
import com.origemite.lib.common.web.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@Tag(name = "631544b3-f2af-4d42-8395-a338915f4d14", description = "Member")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/631544b3-f2af-4d42-8395-a338915f4d14")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberRes.Item>> search(@ModelAttribute @Valid MemberReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberService.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberRes.Id> save(@RequestBody @Valid MemberReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberService.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") String id) {
        return CommonResponseUtils.responseSuccess(memberService.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") String id, @RequestBody MemberReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberService.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") String id) {
        memberService.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberRes.Item>> findAll(@ModelAttribute MemberReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberService.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberRes.Item>> findByIds(@RequestParam @Valid List<String> ids) {
        return CommonResponseUtils.responseSuccess(memberService.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberRes.Name>> searchName(@ModelAttribute @Valid MemberReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberService.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberRes.Name>> findAllName(@ModelAttribute @Valid MemberReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberService.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<String> ids) {
        memberService.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}