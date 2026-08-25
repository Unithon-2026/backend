package com.unithon.meetroute.domain.auth.dto;

import com.unithon.meetroute.domain.user.entity.User;

public record SignupResponse(
        Long id,
        String name,
        String email
) {
    public static SignupResponse from(User user) {
        return new SignupResponse(user.getId(), user.getName(), user.getEmail());
    }
}
