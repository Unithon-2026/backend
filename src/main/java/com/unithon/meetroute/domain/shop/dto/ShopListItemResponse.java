package com.unithon.meetroute.domain.shop.dto;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.entity.Shop;

public record ShopListItemResponse(
        Long id,
        String name,
        String gu,
        String dong,
        String businessType,
        Integer score,
        PriorityGrade priorityGrade
) {
    public static ShopListItemResponse from(Shop shop) {
        return new ShopListItemResponse(
                shop.getId(),
                shop.getName(),
                shop.getGu(),
                shop.getDong(),
                shop.getBusinessType(),
                shop.getScore(),
                shop.getPriorityGrade()
        );
    }
}
