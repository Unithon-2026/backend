package com.unithon.meetroute.domain.shop.briefing.service;

import com.unithon.meetroute.domain.shop.briefing.dto.BriefingResponse;
import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.shop.repository.ShopRepository;
import com.unithon.meetroute.global.exception.BusinessException;
import com.unithon.meetroute.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShopBriefingService {

    // 상권 경쟁 밀집도 임계값
    private static final int LOW_DENSITY_THRESHOLD = 5;
    private static final int HIGH_DENSITY_THRESHOLD = 15;

    // 상호명 기반 육류 메뉴 추정 키워드
    private static final Set<String> MEAT_KEYWORDS = Set.of(
            "고기", "갈비", "삼겹살", "곱창", "정육", "숯불", "육회", "스테이크", "와규", "바베큐", "BBQ"
    );

    private final ShopRepository shopRepository;
    private final ChatClient chatClient;

    public BriefingResponse generateBriefing(Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new BusinessException(ErrorCode.SHOP_NOT_FOUND));

        String userPrompt = buildUserPrompt(shop);

        try {
            long startedAt = System.currentTimeMillis();

            String briefing = chatClient.prompt()
                    .user(userPrompt)
                    .call()
                    .content();

            log.info("[briefing] shopId={} elapsedMs={}", shopId, System.currentTimeMillis() - startedAt);

            return new BriefingResponse(shopId, briefing);
        } catch (Exception e) {
            log.error("AI briefing generation failed for shopId={}", shopId, e);
            throw new BusinessException(ErrorCode.AI_BRIEFING_FAILED);
        }
    }

    private String buildUserPrompt(Shop shop) {
        return """
                다음 매장 정보를 참고해서 영업 접근 브리핑을 작성해줘.

                - 상호명: %s
                - 주소: %s
                - 지역구: %s
                - 동: %s
                - 업태: %s
                - 면적: %s
                - 상권 경쟁 밀집도: %s
                - 상호명 기반 육류 메뉴 가능성: %s
                """.formatted(
                shop.getName(),
                shop.getAddressJibun(),
                shop.getGu(),
                shop.getDong(),
                shop.getBusinessType(),
                shop.getArea(),
                describeCompetitorDensity(shop),
                hasMeatKeyword(shop.getName()) ? "상호명에 육류 관련 키워드 있음 (육류 메뉴 비중 높을 가능성)" : "특이사항 없음"
        );
    }

    private String describeCompetitorDensity(Shop shop) {
        long count = shopRepository.countByGuAndBusinessType(shop.getGu(), shop.getBusinessType());
        String density = count >= HIGH_DENSITY_THRESHOLD ? "매우 높음"
                : count >= LOW_DENSITY_THRESHOLD ? "보통"
                : "낮음";
        return "%s %s 업종 매장 수 %d건 (밀집도: %s)".formatted(shop.getGu(), shop.getBusinessType(), count, density);
    }

    private boolean hasMeatKeyword(String name) {
        if (name == null) {
            return false;
        }
        return MEAT_KEYWORDS.stream().anyMatch(name::contains);
    }
}
