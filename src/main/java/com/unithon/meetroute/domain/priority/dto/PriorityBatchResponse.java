package com.unithon.meetroute.domain.priority.dto;

public record PriorityBatchResponse(
        long totalShopCount,
        long processedCount,
        long elapsedMillis
) {
}
