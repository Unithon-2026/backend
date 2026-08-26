package com.unithon.meetroute.domain.shop.dto;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.entity.Shop;

import java.math.BigDecimal;

public record ShopDetailResponse(
        Long id,
        String name,
        String addressJibun,
        String gu,
        String dong,
        String phone,
        String businessType,
        BigDecimal area,
        BigDecimal longitude,
        BigDecimal latitude,
        Integer score,
        PriorityGrade priorityGrade,
        boolean isBookmarked
) {
    public static ShopDetailResponse from(Shop shop, boolean isBookmarked) {
        return new ShopDetailResponse(
                shop.getId(),
                shop.getName(),
                shop.getAddressJibun(),
                shop.getGu(),
                shop.getDong(),
                shop.getPhone(),
                shop.getBusinessType(),
                shop.getArea(),
                shop.getLongitude(),
                shop.getLatitude(),
                shop.getScore(),
                shop.getPriorityGrade(),
                isBookmarked
        );
    }
}
