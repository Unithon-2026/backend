package com.unithon.meetroute.domain.salesactivity.entity;

import com.unithon.meetroute.domain.shop.entity.Shop;
import com.unithon.meetroute.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "sales_activity")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SalesActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private Shop shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private VisitStatus status;

    @Column(columnDefinition = "TEXT")
    private String memo;

    private LocalDate visitedAt;

    @Builder
    public SalesActivity(Shop shop, User user, VisitStatus status, String memo, LocalDate visitedAt) {
        this.shop = shop;
        this.user = user;
        this.status = status;
        this.memo = memo;
        this.visitedAt = visitedAt;
    }

    public void update(VisitStatus status, String memo, LocalDate visitedAt) {
        this.status = status;
        this.memo = memo;
        this.visitedAt = visitedAt;
    }
}
