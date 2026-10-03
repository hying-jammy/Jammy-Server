package com.jammy.auth.controller;

import com.jammy.auth.dto.SignupRequest;
import com.jammy.auth.service.AuthService;
import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.SuccessCode;
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
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponse<String>> signup(@Valid @RequestBody SignupRequest request) {
        authService.signup(request);
        return ResponseEntity
                .status(SuccessCode.SIGNUP_SUCCESS.getHttpStatus())
                .body(CommonResponse.success(SuccessCode.SIGNUP_SUCCESS, "OK"));
    }
}
