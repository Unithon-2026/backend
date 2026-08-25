package com.unithon.meetroute.domain.shop.controller;

import com.unithon.meetroute.domain.shop.briefing.dto.BriefingResponse;
import com.unithon.meetroute.domain.shop.briefing.service.ShopBriefingService;
import com.unithon.meetroute.domain.shop.dto.ShopDetailResponse;
import com.unithon.meetroute.domain.shop.dto.ShopListItemResponse;
import com.unithon.meetroute.domain.shop.dto.ShopMapMarkerResponse;
import com.unithon.meetroute.domain.shop.service.ShopService;
import com.unithon.meetroute.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "매장 컨트롤러")
@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ShopBriefingService shopBriefingService;

    @GetMapping
    @Operation(summary = "매장 목록 조회", description = "지역구, 업태, 우선순위 등급으로 필터링하고 우선순위 점수 기준으로 정렬된 매장 목록을 페이지 단위로 조회합니다.")
    public ApiResponse<Page<ShopListItemResponse>> list(
            @RequestParam(required = false) String gu,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String priorityGrade,
            @PageableDefault(size = 20, sort = "score", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(shopService.list(gu, businessType, priorityGrade, pageable));
    }

    @GetMapping("/map")
    @Operation(summary = "지도 영역 내 매장 조회", description = "지도 화면에 보이는 영역(bounding box) 안의 매장을 우선순위 점수 기준으로 최대 limit개까지 조회합니다.")
    public ApiResponse<List<ShopMapMarkerResponse>> findInBoundingBox(
            @RequestParam BigDecimal minLatitude,
            @RequestParam BigDecimal maxLatitude,
            @RequestParam BigDecimal minLongitude,
            @RequestParam BigDecimal maxLongitude,
            @RequestParam(required = false) String gu,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String priorityGrade,
            @RequestParam(required = false) Integer limit
    ) {
        return ApiResponse.success(shopService.findInBoundingBox(
                minLatitude, maxLatitude, minLongitude, maxLongitude, gu, businessType, priorityGrade, limit));
    }

    @GetMapping("/{shopId}")
    @Operation(summary = "매장 상세 조회", description = "매장 ID로 인허가 정보, 위치, 우선순위 등 상세 정보를 조회합니다.")
    public ApiResponse<ShopDetailResponse> getDetail(@PathVariable Long shopId) {
        return ApiResponse.success(shopService.getDetail(shopId));
    }

    @PostMapping("/{shopId}/briefing")
    @Operation(summary = "AI 영업 브리핑 생성", description = "매장의 인허가 데이터를 바탕으로 Claude API를 1회 호출해 영업 접근 방법을 요약한 브리핑 텍스트를 생성합니다.")
    public ApiResponse<BriefingResponse> briefing(@PathVariable Long shopId) {
        return ApiResponse.success(shopBriefingService.generateBriefing(shopId));
    }
}
