package com.unithon.meetroute.domain.priority.dto;

import com.unithon.meetroute.domain.priority.entity.Priority;
import com.unithon.meetroute.domain.priority.entity.PriorityGrade;

import java.time.LocalDateTime;

public record PriorityResponse(
        Long shopId,
        int score,
        PriorityGrade grade,
        LocalDateTime calculatedAt
) {
    public static PriorityResponse from(Priority priority) {
        return new PriorityResponse(
                priority.getShop().getId(),
                priority.getScore(),
                priority.getPriorityGrade(),
                priority.getCalculatedAt()
        );
    }
}
