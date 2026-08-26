package com.unithon.meetroute.domain.shop.service;

import com.unithon.meetroute.domain.bookmark.repository.BookmarkRepository;
import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.dto.ShopDetailResponse;
import com.unithon.meetroute.domain.shop.dto.ShopListItemResponse;
import com.unithon.meetroute.domain.shop.dto.ShopMapMarkerResponse;
import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import com.unithon.meetroute.domain.shop.repository.ShopSpecifications;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShopService {

    private static final int DEFAULT_MAP_LIMIT = 500;
    private static final int MAX_MAP_LIMIT = 1000;

    private final ShopRepository shopRepository;
    private final BookmarkRepository bookmarkRepository;

    public Page<ShopListItemResponse> list(String gu, String businessType, String priorityGradeRaw, Pageable pageable, Long userId) {
        PriorityGrade priorityGrade = parsePriorityGrade(priorityGradeRaw);

        Specification<Shop> spec = Specification.allOf(
                ShopSpecifications.hasGu(gu),
                ShopSpecifications.hasBusinessType(businessType),
                ShopSpecifications.hasPriorityGrade(priorityGrade)
        );

        Page<Shop> shops = shopRepository.findAll(spec, pageable);
        Set<Long> bookmarkedShopIds = bookmarkedShopIds(userId, shops.getContent());

        return shops.map(shop -> ShopListItemResponse.from(shop, bookmarkedShopIds.contains(shop.getId())));
    }

    public ShopDetailResponse getDetail(Long shopId, Long userId) {
        Shop shop = findShopOrThrow(shopId);
        boolean isBookmarked = userId != null && bookmarkRepository.existsByShop_IdAndUser_Id(shopId, userId);
        return ShopDetailResponse.from(shop, isBookmarked);
    }

    private Set<Long> bookmarkedShopIds(Long userId, List<Shop> shops) {
        if (userId == null || shops.isEmpty()) {
            return Set.of();
        }
        List<Long> shopIds = shops.stream().map(Shop::getId).toList();
        return new HashSet<>(bookmarkRepository.findShopIdsByUser_IdAndShop_IdIn(userId, shopIds));
    }

    public List<ShopMapMarkerResponse> findInBoundingBox(
            BigDecimal minLatitude, BigDecimal maxLatitude,
            BigDecimal minLongitude, BigDecimal maxLongitude,
            String gu, String businessType, String priorityGradeRaw,
            Integer limit) {

        if (minLatitude == null || maxLatitude == null || minLongitude == null || maxLongitude == null) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "지도 영역(bounding box) 좌표는 모두 필수입니다.");
        }
        if (minLatitude.compareTo(maxLatitude) > 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "minLatitude는 maxLatitude보다 클 수 없습니다.");
        }
        if (minLongitude.compareTo(maxLongitude) > 0) {
            throw new BusinessException(ErrorCode.INVALID_INPUT, "minLongitude는 maxLongitude보다 클 수 없습니다.");
        }

        PriorityGrade priorityGrade = parsePriorityGrade(priorityGradeRaw);
        int effectiveLimit = (limit == null || limit <= 0) ? DEFAULT_MAP_LIMIT : Math.min(limit, MAX_MAP_LIMIT);

        Specification<Shop> spec = Specification.allOf(
                ShopSpecifications.inBoundingBox(minLatitude, maxLatitude, minLongitude, maxLongitude),
                ShopSpecifications.hasGu(gu),
                ShopSpecifications.hasBusinessType(businessType),
                ShopSpecifications.hasPriorityGrade(priorityGrade)
        );

        Pageable pageable = PageRequest.of(0, effectiveLimit, Sort.by(Sort.Direction.DESC, "score"));
        return shopRepository.findAll(spec, pageable).map(ShopMapMarkerResponse::from).getContent();
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
