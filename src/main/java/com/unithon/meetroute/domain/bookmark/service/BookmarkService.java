package com.unithon.meetroute.domain.bookmark.service;

import com.unithon.meetroute.domain.bookmark.entity.Bookmark;
import com.unithon.meetroute.domain.bookmark.repository.BookmarkRepository;
import com.unithon.meetroute.domain.shop.dto.ShopListItemResponse;
import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import com.unithon.meetroute.domain.user.entity.User;
import com.unithon.meetroute.domain.user.repository.UserRepository;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookmarkService {

    private final BookmarkRepository bookmarkRepository;
    private final ShopRepository shopRepository;
    private final UserRepository userRepository;

    @Transactional
    public void add(Long shopId, Long userId) {
        if (bookmarkRepository.existsByShop_IdAndUser_Id(shopId, userId)) {
            return;
        }

        Shop shop = findShopOrThrow(shopId);
        User user = findUserOrThrow(userId);

        bookmarkRepository.save(Bookmark.builder()
                .shop(shop)
                .user(user)
                .createdAt(LocalDateTime.now())
                .build());
    }

    @Transactional
    public void remove(Long shopId, Long userId) {
        Bookmark bookmark = bookmarkRepository.findByShop_IdAndUser_Id(shopId, userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOOKMARK_NOT_FOUND));

        bookmarkRepository.delete(bookmark);
    }

    public Page<ShopListItemResponse> list(Long userId, Pageable pageable) {
        return bookmarkRepository.findByUser_Id(userId, pageable)
                .map(bookmark -> ShopListItemResponse.from(bookmark.getShop(), true));
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
