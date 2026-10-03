package com.jammy.global.exception.handler;

import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.BaseCode;
import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CommonResponse<Void>> handleBusinessException(BusinessException e) {
        BaseCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(CommonResponse.failure(errorCode));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<CommonResponse<Map<String, String>>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() == null
                                ? ErrorCode.BAD_REQUEST.getMessage()
                                : fieldError.getDefaultMessage(),
                        (existingMessage, newMessage) -> existingMessage
                ));

        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getHttpStatus())
                .body(CommonResponse.failure(ErrorCode.BAD_REQUEST, errors));
    }

    @ExceptionHandler({
            HttpMessageNotReadableException.class,
            MissingServletRequestParameterException.class,
            MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class
    })
    protected ResponseEntity<CommonResponse<Void>> handleBadRequestException(Exception e) {
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getHttpStatus())
                .body(CommonResponse.failure(ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<Void>> handleUnexpectedException(Exception e) {
        log.error("Unexpected Error Log", e);

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR));
    }
}
