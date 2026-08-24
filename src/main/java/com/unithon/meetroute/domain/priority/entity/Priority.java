package com.unithon.meetroute.domain.priority.entity;

import com.unithon.meetroute.domain.shop.entity.Shop;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Priority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false, unique = true)
    private Shop shop;

    private int score;

    @Enumerated(EnumType.STRING)
    private PriorityGrade priorityGrade;

    private LocalDateTime calculatedAt;

    public Priority(Shop shop, int score, PriorityGrade priorityGrade, LocalDateTime calculatedAt) {
        this.shop = shop;
        this.score = score;
        this.priorityGrade = priorityGrade;
        this.calculatedAt = calculatedAt;
    }

    public void update(int score, PriorityGrade priorityGrade, LocalDateTime calculatedAt) {
        this.score = score;
        this.priorityGrade = priorityGrade;
        this.calculatedAt = calculatedAt;
    }
}
