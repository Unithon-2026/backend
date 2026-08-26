package com.unithon.meetroute.domain.bookmark.controller;

import com.unithon.meetroute.domain.auth.SessionConst;
import com.unithon.meetroute.domain.bookmark.service.BookmarkService;
import com.unithon.meetroute.domain.shop.dto.ShopListItemResponse;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import com.unithon.meetroute.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "북마크 컨트롤러")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    @PostMapping("/shops/{shopId}/bookmark")
    @Operation(summary = "매장 북마크 추가", description = "로그인한 영업사원이 매장을 북마크에 추가합니다. 이미 북마크한 매장이면 아무 동작 없이 성공 처리합니다.")
    public ApiResponse<Void> add(@PathVariable Long shopId, HttpServletRequest servletRequest) {
        bookmarkService.add(shopId, currentUserId(servletRequest));
        return ApiResponse.success(null);
    }

    @DeleteMapping("/shops/{shopId}/bookmark")
    @Operation(summary = "매장 북마크 해제", description = "로그인한 영업사원이 매장 북마크를 해제합니다. 북마크한 적 없는 매장이면 404를 반환합니다.")
    public ApiResponse<Void> remove(@PathVariable Long shopId, HttpServletRequest servletRequest) {
        bookmarkService.remove(shopId, currentUserId(servletRequest));
        return ApiResponse.success(null);
    }

    @GetMapping("/bookmarks")
    @Operation(summary = "내 북마크 매장 목록 조회", description = "로그인한 영업사원이 북마크한 매장 목록을 최신 북마크순으로 페이지 단위로 조회합니다.")
    public ApiResponse<Page<ShopListItemResponse>> list(
            @PageableDefault(size = 20, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
            HttpServletRequest servletRequest
    ) {
        return ApiResponse.success(bookmarkService.list(currentUserId(servletRequest), pageable));
    }

    private Long currentUserId(HttpServletRequest servletRequest) {
        HttpSession session = servletRequest.getSession(false);
        Long userId = session != null ? (Long) session.getAttribute(SessionConst.LOGIN_USER_ID) : null;
        if (userId == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }
        return userId;
    }
}
