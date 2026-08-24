package com.unithon.meetroute.domain.shop.service;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.dto.ShopDetailResponse;
import com.unithon.meetroute.domain.shop.dto.ShopListItemResponse;
import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import com.unithon.meetroute.domain.shop.repository.ShopSpecifications;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private final ShopRepository shopRepository;

    public Page<ShopListItemResponse> list(String gu, String businessType, String priorityGradeRaw, Pageable pageable) {
        PriorityGrade priorityGrade = parsePriorityGrade(priorityGradeRaw);

        Specification<Shop> spec = Specification.allOf(
                ShopSpecifications.hasGu(gu),
                ShopSpecifications.hasBusinessType(businessType),
                ShopSpecifications.hasPriorityGrade(priorityGrade)
        );

        return shopRepository.findAll(spec, pageable).map(ShopListItemResponse::from);
    }

    public ShopDetailResponse getDetail(Long shopId) {
        return ShopDetailResponse.from(findShopOrThrow(shopId));
    }

    private Shop findShopOrThrow(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOP_NOT_FOUND));
    }

    private PriorityGrade parsePriorityGrade(String raw) {
        if (raw == null || raw.isBlank()) {
            return null;
        }
        try {
            return PriorityGrade.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "priorityGrade 값이 올바르지 않습니다: " + raw);
        }
    }
}
