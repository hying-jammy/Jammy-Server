package com.jammy.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.jammy.global.common.code.BaseCode;

public record CommonResponse<T>(
        boolean isSuccess,
        int code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        T result
) {
    public static <T> CommonResponse<T> success(BaseCode successCode, T data) {
        return new CommonResponse<>(true, successCode.getStatus(), successCode.getMessage(), data);
    }

    public static CommonResponse<String> success(BaseCode successCode) {
        return new CommonResponse<>(true, successCode.getStatus(), successCode.getMessage(), "OK");
    }

    public static <T> CommonResponse<T> failure(BaseCode errorCode, T data) {
        return new CommonResponse<>(false, errorCode.getStatus(), errorCode.getMessage(), data);
    }

    public static CommonResponse<Void> failure(BaseCode errorCode) {
        return new CommonResponse<>(false, errorCode.getStatus(), errorCode.getMessage(), null);
    }
}
