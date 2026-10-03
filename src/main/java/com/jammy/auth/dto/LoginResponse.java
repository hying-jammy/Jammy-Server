package com.jammy.auth.dto;

import com.jammy.user.domain.User;

public record LoginResponse(
        Long userId,
        String nickname
) {
    public static LoginResponse from(User user) {
        return new LoginResponse(user.getId(), user.getNickname());
    }
}
