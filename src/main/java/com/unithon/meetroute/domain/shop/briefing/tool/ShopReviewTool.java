package com.unithon.meetroute.domain.shop.briefing.tool;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

@Component
public class ShopReviewTool {

    @Tool(description = "매장의 리뷰 요약 정보를 조회합니다.")
    public String getShopReviewSummary(Long shopId) {
        return "리뷰 데이터 소스가 아직 연동되지 않았습니다.";
    }
}
