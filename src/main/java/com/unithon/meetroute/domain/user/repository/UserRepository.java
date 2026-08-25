package com.unithon.meetroute.domain.user.repository;

import com.unithon.meetroute.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
