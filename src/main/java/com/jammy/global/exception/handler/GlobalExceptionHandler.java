package com.jammy.global.exception.handler;

import com.jammy.global.common.CommonResponse;
import com.jammy.global.common.code.BaseCode;
import com.jammy.global.common.code.ErrorCode;
import com.jammy.global.exception.BusinessException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.method.ParameterErrors;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<CommonResponse<Void>> handleBusinessException(BusinessException e) {
        BaseCode errorCode = e.getErrorCode();
        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(CommonResponse.failure(errorCode));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = getFieldErrors(e.getBindingResult().getFieldErrors());
        return handleExceptionInternal(e, CommonResponse.failure(ErrorCode.BAD_REQUEST, errors), headers, status, request);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<CommonResponse<Map<String, String>>> handleConstraintViolationException(ConstraintViolationException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (var violation : e.getConstraintViolations()) {
            String path = violation.getPropertyPath().toString();
            String field = path.substring(path.lastIndexOf('.') + 1);
            errors.putIfAbsent(field, getValidationMessage(violation.getMessage()));
        }
        return ResponseEntity
                .status(ErrorCode.BAD_REQUEST.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(CommonResponse.failure(ErrorCode.BAD_REQUEST, errors));
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException e, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        if (e.isForReturnValue()) {
            return super.handleHandlerMethodValidationException(e, headers, status, request);
        }

        Map<String, String> errors = getParameterErrors(e);
        return handleExceptionInternal(e, CommonResponse.failure(ErrorCode.BAD_REQUEST, errors), headers, status, request);
    }

    @Override
    protected ResponseEntity<Object> createResponseEntity(
            Object body, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.putAll(headers);
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);
        if (!(body instanceof CommonResponse<?>)) {
            ErrorCode errorCode = getErrorCode(status);
            body = new CommonResponse<>(false, status.value(), errorCode.getMessage(), null);
        }
        return new ResponseEntity<>(body, responseHeaders, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<CommonResponse<Void>> handleUnexpectedException(Exception e) {
        log.error("Unexpected Error Log", e);
        return ResponseEntity
                .status(ErrorCode.INTERNAL_SERVER_ERROR.getHttpStatus())
                .contentType(MediaType.APPLICATION_JSON)
                .body(CommonResponse.failure(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private Map<String, String> getParameterErrors(HandlerMethodValidationException e) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (var result : e.getParameterValidationResults()) {
            if (result instanceof ParameterErrors parameterErrors) {
                getFieldErrors(parameterErrors.getFieldErrors()).forEach(errors::putIfAbsent);
            } else {
                String field = result.getMethodParameter().getParameterName();
                field = field == null ? "request" : field;
                for (var error : result.getResolvableErrors()) {
                    errors.putIfAbsent(field, getValidationMessage(error.getDefaultMessage()));
                }
            }
        }
        for (var error : e.getCrossParameterValidationResults()) {
            errors.putIfAbsent("request", getValidationMessage(error.getDefaultMessage()));
        }
        return errors;
    }

    private ErrorCode getErrorCode(HttpStatusCode status) {
        return switch (status.value()) {
            case 400 -> ErrorCode.BAD_REQUEST;
            case 404 -> ErrorCode.NOT_FOUND;
            case 405 -> ErrorCode.METHOD_NOT_ALLOWED;
            case 406 -> ErrorCode.NOT_ACCEPTABLE;
            case 413 -> ErrorCode.PAYLOAD_TOO_LARGE;
            case 415 -> ErrorCode.UNSUPPORTED_MEDIA_TYPE;
            default -> status.is5xxServerError() ? ErrorCode.INTERNAL_SERVER_ERROR : ErrorCode.BAD_REQUEST;
        };
    }

    private Map<String, String> getFieldErrors(List<FieldError> fieldErrors) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (FieldError error : fieldErrors) {
            errors.putIfAbsent(error.getField(), getValidationMessage(error.getDefaultMessage()));
        }
        return errors;
    }

    private String getValidationMessage(String message) {
        return message == null ? ErrorCode.BAD_REQUEST.getMessage() : message;
    }
}
