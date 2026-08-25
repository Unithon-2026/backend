package com.unithon.meetroute.domain.salesactivity.dto;

import com.unithon.meetroute.domain.salesactivity.entity.VisitStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RecordVisitRequest(
        @NotNull(message = "방문 단계를 선택해주세요.")
        VisitStatus status,

        LocalDate visitedAt
) {
}
