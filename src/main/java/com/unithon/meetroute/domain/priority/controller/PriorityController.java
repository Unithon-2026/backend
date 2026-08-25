package com.unithon.meetroute.domain.priority.controller;

import com.unithon.meetroute.domain.priority.dto.PriorityResponse;
import com.unithon.meetroute.domain.priority.service.PriorityService;
import com.unithon.meetroute.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "우선순위 컨트롤러")
@RestController
@RequestMapping("/api/v1/shops/{shopId}/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    @Operation(summary = "매장 우선순위 조회", description = "이미 계산되어 저장된 매장의 우선순위 점수와 등급을 조회합니다. 아직 계산된 값이 없으면 404를 반환합니다.")
    public ApiResponse<PriorityResponse> getPriority(@PathVariable Long shopId) {
        return ApiResponse.success(priorityService.getPriority(shopId));
    }

    @PostMapping
    @Operation(summary = "매장 우선순위 계산", description = "해당 매장의 우선순위를 다시 계산해 저장하고, 결과 점수와 등급을 반환합니다.")
    public ApiResponse<PriorityResponse> calculatePriority(@PathVariable Long shopId) {
        return ApiResponse.success(priorityService.calculateAndUpsert(shopId));
    }
}
