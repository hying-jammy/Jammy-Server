package com.jammy.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원가입 요청")
public record SignupRequest(
        @Schema(description = "친구들에게 보일 닉네임", example = "재미")
        @NotBlank
        @Size(max = 20)
        String nickname,

        @Schema(description = "로그인에 사용할 이메일", example = "jammy@email.com")
        @NotBlank
        @Email
        @Size(max = 50)
        String email,

        @Schema(description = "영문과 숫자를 포함한 8자 이상의 비밀번호", example = "jammy1234")
        @NotBlank
        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).{8,}$")
        String password
) {
}
