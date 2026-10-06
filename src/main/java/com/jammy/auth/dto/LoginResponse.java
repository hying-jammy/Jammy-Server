package com.jammy.auth.dto;

import com.jammy.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "로그인 응답")
public record LoginResponse(
        @Schema(description = "사용자 식별자", example = "1")
        Long userId,

        @Schema(description = "사용자 닉네임", example = "재미")
        String nickname
) {
    public static LoginResponse from(User user) {
        return new LoginResponse(user.getId(), user.getNickname());
    }
}
