package com.unithon.meetroute.domain.salesactivity.service;

import com.unithon.meetroute.domain.salesactivity.dto.RecordVisitRequest;
import com.unithon.meetroute.domain.salesactivity.dto.SalesActivityResponse;
import com.unithon.meetroute.domain.salesactivity.dto.UpdateMemoRequest;
import com.unithon.meetroute.domain.salesactivity.entity.SalesActivity;
import com.unithon.meetroute.domain.salesactivity.repository.SalesActivityRepository;
import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import com.unithon.meetroute.domain.user.entity.User;
import com.unithon.meetroute.domain.user.repository.UserRepository;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SalesActivityService {

    private final ShopRepository shopRepository;
    private final UserRepository userRepository;
    private final SalesActivityRepository salesActivityRepository;

    @Transactional
    public SalesActivityResponse record(Long shopId, Long userId, RecordVisitRequest request) {
        Shop shop = findShopOrThrow(shopId);
        User user = findUserOrThrow(userId);
        LocalDate visitedAt = request.visitedAt() != null ? request.visitedAt() : LocalDate.now();

        SalesActivity activity = salesActivityRepository
                .findByShop_IdAndUser_IdAndStatus(shopId, userId, request.status())
                .map(existing -> {
                    existing.recordVisit(request.status(), visitedAt);
                    return existing;
                })
                .orElseGet(() -> salesActivityRepository.save(
                        SalesActivity.builder()
                                .shop(shop)
                                .user(user)
                                .status(request.status())
                                .visitedAt(visitedAt)
                                .build()
                ));

        return SalesActivityResponse.from(activity);
    }

    @Transactional
    public SalesActivityResponse updateMemo(Long shopId, Long userId, UpdateMemoRequest request) {
        SalesActivity activity = salesActivityRepository
                .findByShop_IdAndUser_IdAndStatus(shopId, userId, request.status())
                .orElseThrow(() -> new BusinessException(ErrorCode.SALES_ACTIVITY_NOT_FOUND));

        activity.updateMemo(request.memo());

        return SalesActivityResponse.from(activity);
    }

    public List<SalesActivityResponse> list(Long shopId, Long userId) {
        findShopOrThrow(shopId);

        return salesActivityRepository.findByShop_IdAndUser_IdOrderByVisitedAtDesc(shopId, userId).stream()
                .map(SalesActivityResponse::from)
                .toList();
    }

    private Shop findShopOrThrow(Long shopId) {
        return shopRepository.findById(shopId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOP_NOT_FOUND));
    }

    private User findUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
    }
}
