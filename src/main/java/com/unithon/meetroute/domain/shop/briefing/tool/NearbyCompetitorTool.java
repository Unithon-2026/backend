package com.unithon.meetroute.domain.shop.briefing.tool;

import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;

/**
 * 같은 지역구/업태 매장 수로 상권 경쟁 밀집도를 추정하는 tool.
 * 밀집도가 높을수록 "원가 절감 니즈가 클 것" 같은 접근 전략 힌트로 쓰인다.
 * 임계값은 튜닝 가능하도록 상수로 분리.
 */
@Component
@RequiredArgsConstructor
public class NearbyCompetitorTool {

    private static final int LOW_DENSITY_THRESHOLD = 5;
    private static final int HIGH_DENSITY_THRESHOLD = 15;

    private final ShopRepository shopRepository;

    @Tool(description = "같은 지역구/업태의 주변 동종 업종 매장 수를 조회해 상권 경쟁 밀집도를 파악합니다.")
    public String getNearbyCompetitors(
            @ToolParam(description = "지역구 (예: 강남구)") String gu,
            @ToolParam(description = "업태명 (예: 한식음식점)") String businessType
    ) {
        long count = shopRepository.countByGuAndBusinessType(gu, businessType);

        String density = count >= HIGH_DENSITY_THRESHOLD ? "매우 높음"
                : count >= LOW_DENSITY_THRESHOLD ? "보통"
                : "낮음";

        return "%s %s 업종 매장 수: %d건, 경쟁 밀집도: %s".formatted(gu, businessType, count, density);
    }
}
