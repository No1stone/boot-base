package com.origemite.apiauth.member;

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

@Tag(name = "7e222a45-3e1f-46b8-849e-d4a843f2ac3a", description = "MemberLoginHistory")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/7e222a45-3e1f-46b8-849e-d4a843f2ac3a")
public class MemberLoginHistoryController {

    private final MemberLoginHistoryService memberLoginHistoryService;

    @Operation(summary = "목록 조회")
    @GetMapping
    public CommonResponse<Page<MemberLoginHistoryRes.Item>> search(@ModelAttribute @Valid MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.search(filter, pageable));
    }

    @Operation(summary = "생성")
    @PostMapping
    public CommonResponse<MemberLoginHistoryRes.Id> save(@RequestBody @Valid MemberLoginHistoryReq.Create create) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.save(create));
    }

    @Operation(summary = "단건 조회")
    @GetMapping("/{id}")
    public CommonResponse<MemberLoginHistoryRes.Item> findById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.findById(id));
    }

    @Operation(summary = "수정")
    @PutMapping("/{id}")
    public CommonResponse<MemberLoginHistoryRes.Id> saveById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id, @RequestBody MemberLoginHistoryReq.Update update) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.saveById(id, update));
    }

    @Operation(summary = "삭제")
    @DeleteMapping("/{id}")
    public CommonResponse<?> deleteById(@Parameter(description = "ID") @PathVariable(name = "id") Integer id) {
        memberLoginHistoryService.deleteById(id);
        return CommonResponseUtils.responseSuccess();
    }

    @Operation(summary = "전체 조회")
    @GetMapping("/find-all")
    public CommonResponse<List<MemberLoginHistoryRes.Item>> findAll(@ModelAttribute MemberLoginHistoryReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.findAll(filter));
    }

    @Operation(summary = "아이디 선택 조회")
    @GetMapping("/find-by-ids")
    public CommonResponse<List<MemberLoginHistoryRes.Item>> findByIds(@RequestParam @Valid List<Integer> ids) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.findByIds(ids));
    }

    @Operation(summary = "이름 검색")
    @GetMapping("/search-name")
    public CommonResponse<Page<MemberLoginHistoryRes.Name>> searchName(@ModelAttribute @Valid MemberLoginHistoryReq.Filter filter, Pageable pageable) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.searchName(filter, pageable));
    }

    @Operation(summary = "이름 전체 조회")
    @GetMapping("/find-all-name")
    public CommonResponse<List<MemberLoginHistoryRes.Name>> findAllName(@ModelAttribute @Valid MemberLoginHistoryReq.Filter filter) {
        return CommonResponseUtils.responseSuccess(memberLoginHistoryService.findAllName(filter));
    }

    @Operation(summary = "아이디 선택 삭제")
    @DeleteMapping("/delete-by-ids")
    public CommonResponse<?> deleteByIds(@RequestParam @Valid List<Integer> ids) {
        memberLoginHistoryService.deleteByIds(ids);
        return CommonResponseUtils.responseSuccess();
    }
}