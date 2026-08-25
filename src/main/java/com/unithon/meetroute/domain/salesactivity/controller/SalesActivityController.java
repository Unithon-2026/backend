package com.unithon.meetroute.domain.salesactivity.controller;

import com.unithon.meetroute.domain.auth.SessionConst;
import com.unithon.meetroute.domain.salesactivity.dto.RecordVisitRequest;
import com.unithon.meetroute.domain.salesactivity.dto.SalesActivityResponse;
import com.unithon.meetroute.domain.salesactivity.dto.UpdateMemoRequest;
import com.unithon.meetroute.domain.salesactivity.service.SalesActivityService;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import com.unithon.meetroute.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "영업활동 컨트롤러")
@RestController
@RequestMapping("/api/v1/shops/{shopId}/sales_activity")
@RequiredArgsConstructor
public class SalesActivityController {

    private final SalesActivityService salesActivityService;

    @PostMapping
    @Operation(summary = "가게 방문 기록", description = "로그인한 영업사원이 해당 매장의 특정 방문 단계를 기록합니다. 이미 기록된 단계면 방문일만 갱신합니다.")
    public ApiResponse<SalesActivityResponse> record(
            @PathVariable Long shopId,
            @Valid @RequestBody RecordVisitRequest request,
            HttpServletRequest servletRequest
    ) {
        Long userId = currentUserId(servletRequest);
        return ApiResponse.success(salesActivityService.record(shopId, userId, request));
    }

    @PostMapping("/memo")
    @Operation(summary = "가게 방문 메모", description = "특정 방문 단계의 메모를 기록합니다. 해당 단계의 방문 기록이 먼저 있어야 합니다.")
    public ApiResponse<SalesActivityResponse> updateMemo(
            @PathVariable Long shopId,
            @Valid @RequestBody UpdateMemoRequest request,
            HttpServletRequest servletRequest
    ) {
        Long userId = currentUserId(servletRequest);
        return ApiResponse.success(salesActivityService.updateMemo(shopId, userId, request));
    }

    @GetMapping
    @Operation(summary = "가게별 방문 이력 목록 조회", description = "로그인한 영업사원 본인이 해당 매장을 방문한 이력을 최신순으로 조회합니다.")
    public ApiResponse<List<SalesActivityResponse>> list(
            @PathVariable Long shopId,
            HttpServletRequest servletRequest
    ) {
        Long userId = currentUserId(servletRequest);
        return ApiResponse.success(salesActivityService.list(shopId, userId));
    }

    private Long currentUserId(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        Long userId = session != null ? (Long) session.getAttribute(SessionConst.LOGIN_USER_ID) : null;
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return userId;
    }
}
