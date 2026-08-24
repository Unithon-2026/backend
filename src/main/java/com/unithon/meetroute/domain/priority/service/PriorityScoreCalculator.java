package com.unithon.meetroute.domain.priority.service;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import com.unithon.meetroute.domain.shop.entity.Shop;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 규칙 기반 우선순위 점수 계산기. Sales_Activity(방문 이력) 데이터가 아직 없어
 * 지금은 Shop 컬럼만으로 채점한다. 아래 가중치/임계값은 바로 튜닝하도록
 * 파일 상단에 몰아둔 것 — 실제 CSV의 업태 값 종류를 확인한 뒤 목록을 확정해야 한다.
 */
@Component
public class PriorityScoreCalculator {

    private static final Map<String, Integer> BUSINESS_TYPE_WEIGHTS = Map.of(
            "한식음식점", 50,
            "육류,고기요리 전문점", 50,
            "일반음식점", 30
    );
    private static final int BUSINESS_TYPE_DEFAULT_WEIGHT = 0;

    private static final BigDecimal AREA_LARGE_THRESHOLD = new BigDecimal("200");
    private static final BigDecimal AREA_MEDIUM_THRESHOLD = new BigDecimal("100");
    private static final BigDecimal AREA_SMALL_THRESHOLD = new BigDecimal("50");
    private static final int AREA_LARGE_SCORE = 30;
    private static final int AREA_MEDIUM_SCORE = 20;
    private static final int AREA_SMALL_SCORE = 10;
    private static final int AREA_NONE_SCORE = 0;

    private static final int PHONE_PRESENT_SCORE = 20;

    private static final int GRADE_S_THRESHOLD = 80;
    private static final int GRADE_A_THRESHOLD = 60;
    private static final int GRADE_B_THRESHOLD = 40;

    public Result calculate(Shop shop) {
        int score = businessTypeScore(shop.getBusinessType())
                + areaScore(shop.getArea())
                + phoneScore(shop.getPhone());

        return new Result(score, gradeOf(score));
    }

    private int businessTypeScore(String businessType) {
        if (businessType == null) {
            return BUSINESS_TYPE_DEFAULT_WEIGHT;
        }
        return BUSINESS_TYPE_WEIGHTS.getOrDefault(businessType, BUSINESS_TYPE_DEFAULT_WEIGHT);
    }

    private int areaScore(BigDecimal area) {
        if (area == null) {
            return AREA_NONE_SCORE;
        }
        if (area.compareTo(AREA_LARGE_THRESHOLD) >= 0) {
            return AREA_LARGE_SCORE;
        }
        if (area.compareTo(AREA_MEDIUM_THRESHOLD) >= 0) {
            return AREA_MEDIUM_SCORE;
        }
        if (area.compareTo(AREA_SMALL_THRESHOLD) >= 0) {
            return AREA_SMALL_SCORE;
        }
        return AREA_NONE_SCORE;
    }

    private int phoneScore(String phone) {
        return StringUtils.hasText(phone) ? PHONE_PRESENT_SCORE : 0;
    }

    private PriorityGrade gradeOf(int score) {
        if (score >= GRADE_S_THRESHOLD) {
            return PriorityGrade.S;
        }
        if (score >= GRADE_A_THRESHOLD) {
            return PriorityGrade.A;
        }
        if (score >= GRADE_B_THRESHOLD) {
            return PriorityGrade.B;
        }
        return PriorityGrade.C;
    }

    public record Result(int score, PriorityGrade grade) {
    }
}
