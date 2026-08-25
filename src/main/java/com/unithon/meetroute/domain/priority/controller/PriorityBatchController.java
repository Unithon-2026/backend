package com.unithon.meetroute.domain.priority.controller;

import com.unithon.meetroute.domain.priority.dto.PriorityBatchResponse;
import com.unithon.meetroute.domain.priority.service.PriorityBatchService;
import com.unithon.meetroute.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "우선순위 배치 컨트롤러")
@RestController
@RequestMapping("/api/v1/priorities")
@RequiredArgsConstructor
public class PriorityBatchController {

    private final PriorityBatchService priorityBatchService;

    @PostMapping("/batch")
    @Operation(summary = "전체 매장 우선순위 일괄 계산", description = "등록된 모든 매장의 우선순위를 페이지 단위로 일괄 계산해 저장합니다. 매장 수가 많으면 응답까지 수 분이 걸릴 수 있습니다.")
    public ApiResponse<PriorityBatchResponse> calculateAllPriorities() {
        return ApiResponse.success(priorityBatchService.calculateAllAndUpsert());
    }
}
