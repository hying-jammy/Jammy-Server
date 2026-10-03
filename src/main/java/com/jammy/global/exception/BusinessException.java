package com.jammy.global.exception;

import com.jammy.global.common.code.BaseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final BaseCode errorCode;

    public BusinessException(BaseCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
