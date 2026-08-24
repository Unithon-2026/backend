package com.unithon.meetroute.domain.shop.entity;

import com.unithon.meetroute.domain.priority.entity.PriorityGrade;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String addressJibun;

    private String gu;

    private String dong;

    private String phone;

    /**
     * 서울시 공공데이터의 위생업태명 원본 텍스트. 값의 종류/개수를 사전에 알 수 없어
     * Java enum 대신 String으로 저장한다 (CSV에 없던 값이 들어와도 삽입이 깨지지 않게).
     */
    private String businessType;

    @Column(precision = 10, scale = 2)
    private BigDecimal area;

    @Column(precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(precision = 10, scale = 6)
    private BigDecimal latitude;

    /**
     * Priority 테이블 계산 결과를 캐싱한 컬럼. PriorityService.calculateAndUpsert()에서만 갱신된다.
     * GET /shops의 sort=score,desc / priorityGrade 필터를 조인 없이 처리하기 위한 비정규화.
     */
    private Integer score;

    @Enumerated(EnumType.STRING)
    private PriorityGrade priorityGrade;

    @Builder
    public Shop(String name, String addressJibun, String gu, String dong, String phone,
                String businessType, BigDecimal area, BigDecimal longitude, BigDecimal latitude) {
        this.name = name;
        this.addressJibun = addressJibun;
        this.gu = gu;
        this.dong = dong;
        this.phone = phone;
        this.businessType = businessType;
        this.area = area;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public void applyPriority(Integer score, PriorityGrade priorityGrade) {
        this.score = score;
        this.priorityGrade = priorityGrade;
    }
}
