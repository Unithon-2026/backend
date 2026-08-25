package com.unithon.meetroute.domain.salesactivity.dto;

import com.unithon.meetroute.domain.salesactivity.entity.SalesActivity;
import com.unithon.meetroute.domain.salesactivity.entity.VisitStatus;

import java.time.LocalDate;

public record SalesActivityResponse(
        Long id,
        Long shopId,
        VisitStatus status,
        String memo,
        LocalDate visitedAt
) {
    public static SalesActivityResponse from(SalesActivity salesActivity) {
        return new SalesActivityResponse(
                salesActivity.getId(),
                salesActivity.getShop().getId(),
                salesActivity.getStatus(),
                salesActivity.getMemo(),
                salesActivity.getVisitedAt()
        );
    }
}
