package com.unithon.meetroute.domain.priority.controller;

import com.unithon.meetroute.domain.priority.dto.PriorityResponse;
import com.unithon.meetroute.domain.priority.service.PriorityService;
import com.unithon.meetroute.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/shops/{shopId}/priority")
@RequiredArgsConstructor
public class PriorityController {

    private final PriorityService priorityService;

    @GetMapping
    public ApiResponse<PriorityResponse> getPriority(@PathVariable Long shopId) {
        return ApiResponse.success(priorityService.getPriority(shopId));
    }

    @PostMapping
    public ApiResponse<PriorityResponse> calculatePriority(@PathVariable Long shopId) {
        return ApiResponse.success(priorityService.calculateAndUpsert(shopId));
    }
}
