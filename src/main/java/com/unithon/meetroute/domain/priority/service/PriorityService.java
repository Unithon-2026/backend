package com.unithon.meetroute.domain.priority.service;

import com.unithon.meetroute.domain.priority.dto.PriorityResponse;
import com.unithon.meetroute.domain.priority.entity.Priority;
import com.unithon.meetroute.domain.priority.repository.PriorityRepository;
import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PriorityService {

    private final ShopRepository shopRepository;
    private final PriorityRepository priorityRepository;
    private final PriorityScoreCalculator priorityScoreCalculator;

    public PriorityResponse getPriority(Long shopId) {
        Shop shop = findShopOrThrow(shopId);
        Priority priority = priorityRepository.findByShop_Id(shop.getId())
                .orElseThrow(() -> new BusinessException(ErrorCode.PRIORITY_NOT_FOUND));
        return PriorityResponse.from(priority);
    }

    @Transactional
    public PriorityResponse calculateAndUpsert(Long shopId) {
        Shop shop = findShopOrThrow(shopId);

        PriorityScoreCalculator.Result result = priorityScoreCalculator.calculate(shop);
        LocalDateTime now = LocalDateTime.now();

        Priority priority = priorityRepository.findByShop_Id(shop.getId())
                .map(existing -> {
                    existing.update(result.score(), result.grade(), now);
                    return existing;
                })
                .orElseGet(() -> priorityRepository.save(
                        Priority.builder()
                                .shop(shop)
                                .score(result.score())
                                .priorityGrade(result.grade())
                                .calculatedAt(now)
                                .build()
                ));

        shop.applyPriority(result.score(), result.grade());

        return PriorityResponse.from(priority);
    }

    private Shop findShopOrThrow(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOP_NOT_FOUND));
    }
}
