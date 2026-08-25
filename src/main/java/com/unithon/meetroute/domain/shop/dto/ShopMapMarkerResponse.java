package com.unithon.meetroute.domain.shop.dto;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.entity.Shop;

import java.math.BigDecimal;

public record ShopMapMarkerResponse(
        Long id,
        String name,
        String businessType,
        String addressJibun,
        String phone,
        BigDecimal latitude,
        BigDecimal longitude,
        Integer score,
        PriorityGrade priorityGrade
) {
    public static ShopMapMarkerResponse from(Shop shop) {
        return new ShopMapMarkerResponse(
                shop.getId(),
                shop.getName(),
                shop.getBusinessType(),
                shop.getAddressJibun(),
                shop.getPhone(),
                shop.getLatitude(),
                shop.getLongitude(),
                shop.getScore(),
                shop.getPriorityGrade()
        );
    }
}
