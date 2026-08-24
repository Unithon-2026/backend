package com.unithon.meetroute.domain.shop.controller;

import com.unithon.meetroute.domain.shop.briefing.dto.BriefingResponse;
import com.unithon.meetroute.domain.shop.briefing.service.ShopBriefingService;
import com.unithon.meetroute.domain.shop.dto.ShopDetailResponse;
import com.unithon.meetroute.domain.shop.dto.ShopListItemResponse;
import com.unithon.meetroute.domain.shop.service.ShopService;
import com.unithon.meetroute.global.response.ApiResponse;
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

@RestController
@RequestMapping("/api/v1/shops")
@RequiredArgsConstructor
public class ShopController {

    private final ShopService shopService;
    private final ShopBriefingService shopBriefingService;

    @GetMapping
    public ApiResponse<Page<ShopListItemResponse>> list(
            @RequestParam(required = false) String gu,
            @RequestParam(required = false) String businessType,
            @RequestParam(required = false) String priorityGrade,
            @PageableDefault(size = 20, sort = "score", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ApiResponse.success(shopService.list(gu, businessType, priorityGrade, pageable));
    }

    @GetMapping("/{shopId}")
    public ApiResponse<ShopDetailResponse> getDetail(@PathVariable Long shopId) {
        return ApiResponse.success(shopService.getDetail(shopId));
    }

    @PostMapping("/{shopId}/briefing")
    public ApiResponse<BriefingResponse> briefing(@PathVariable Long shopId) {
        return ApiResponse.success(shopBriefingService.generateBriefing(shopId));
    }
}
