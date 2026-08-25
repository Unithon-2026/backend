package com.unithon.meetroute.domain.priority.controller;

import com.unithon.meetroute.domain.priority.dto.PriorityBatchResponse;
import com.unithon.meetroute.domain.priority.service.PriorityBatchService;
import com.unithon.meetroute.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/priorities")
@RequiredArgsConstructor
public class PriorityBatchController {

    private final PriorityBatchService priorityBatchService;

    @PostMapping("/batch")
    public ApiResponse<PriorityBatchResponse> calculateAllPriorities() {
        return ApiResponse.success(priorityBatchService.calculateAllAndUpsert());
    }
}
