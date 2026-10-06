package com.jammy.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "가입한 이메일", example = "jammy@email.com")
        @NotBlank
        @Email
        String email,

        @Schema(description = "로그인 비밀번호", example = "jammy1234")
        @NotBlank
        String password
) {
}
