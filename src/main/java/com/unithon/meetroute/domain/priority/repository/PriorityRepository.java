package com.unithon.meetroute.domain.priority.repository;

import com.unithon.meetroute.domain.priority.entity.Priority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PriorityRepository extends JpaRepository<Priority, Long> {

    Optional<Priority> findByShop_Id(Long shopId);
}
