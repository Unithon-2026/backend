package com.unithon.meetroute.domain.salesactivity.repository;

import com.unithon.meetroute.domain.salesactivity.entity.SalesActivity;
import com.unithon.meetroute.domain.salesactivity.entity.VisitStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalesActivityRepository extends JpaRepository<SalesActivity, Long> {

    Optional<SalesActivity> findByShop_IdAndUser_IdAndStatus(Long shopId, Long userId, VisitStatus status);

    List<SalesActivity> findByShop_IdAndUser_IdOrderByVisitedAtDesc(Long shopId, Long userId);
}
