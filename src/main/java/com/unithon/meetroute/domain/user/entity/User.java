package com.unithon.meetroute.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 100, nullable = false, unique = true)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(length = 100, nullable = false)
    private String password;

    private Long assignedRegionId;

    @Builder
    public User(String name, String email, String phone, String password, Long assignedRegionId) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.assignedRegionId = assignedRegionId;
    }
}
