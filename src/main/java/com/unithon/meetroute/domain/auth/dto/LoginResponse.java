package com.unithon.meetroute.domain.auth.dto;

import com.unithon.meetroute.domain.user.entity.User;

public record LoginResponse(
        Long id,
        String name,
        String email
) {
    public static LoginResponse from(User user) {
        return new LoginResponse(user.getId(), user.getName(), user.getEmail());
    }
}
