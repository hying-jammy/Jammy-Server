package com.jammy.auth.controller;

import com.jammy.auth.dto.LoginRequest;
import com.jammy.auth.dto.LoginResponse;
import com.jammy.auth.dto.SignupRequest;
import com.jammy.auth.service.AuthService;
import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.SuccessCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Tag(name = "[인증]", description = "회원가입 및 로그인 API")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "회원가입",
            description = """
                    닉네임, 이메일, 비밀번호를 입력해 회원가입을 진행합니다.\n
                    **비밀번호는 영문과 숫자를 포함해 8자 이상이어야 합니다.**
                    """
    )
    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<String>> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity
                .status(SuccessCode.SIGNUP_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.SIGNUP_SUCCESS, "OK"));
    }

    @Operation(
            summary = "로그인",
            description = "이메일과 비밀번호로 로그인합니다. 성공 시 사용자 식별자와 닉네임을 반환합니다."
    )
    @PostMapping("/login")
    public ResponseEntity<CommonResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity
                .status(SuccessCode.LOGIN_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.LOGIN_SUCCESS, response));
    }
}
