package com.network.common.exception;

import com.network.common.base.BaseResponse;
import com.network.common.constant.ResultCode;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalException.class)
    public BaseResponse<Void> handleGlobalException(GlobalException e) {
        log.warn("Business exception: code={}, message={}", e.getCode(), e.getMessage());
        return BaseResponse.failed(e.getCode(), e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<Void> handleValidationException(MethodArgumentNotValidException e) {
        String message = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));
        log.warn("Parameter validation failed: {}", message);
        return BaseResponse.failed(ResultCode.VALIDATION_FAILED.getCode(), message);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public BaseResponse<Void> handleConstraintViolation(ConstraintViolationException e) {
        String message = e.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining(", "));
        return BaseResponse.failed(ResultCode.VALIDATION_FAILED.getCode(), message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse<Void> handleMissingParam(MissingServletRequestParameterException e) {
        String message = "Required parameter '" + e.getParameterName() + "' is missing";
        return BaseResponse.failed(ResultCode.VALIDATION_FAILED.getCode(), message);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public BaseResponse<Void> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        String message = "Parameter '" + e.getName() + "' has invalid type";
        return BaseResponse.failed(ResultCode.VALIDATION_FAILED.getCode(), message);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse<Void> handleMessageNotReadable(HttpMessageNotReadableException e) {
        return BaseResponse.failed(ResultCode.VALIDATION_FAILED.getCode(), "Request body is invalid or missing");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException e) {
        return BaseResponse.failed(ResultCode.METHOD_NOT_SUPPORTED);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public BaseResponse<Void> handleMediaTypeNotSupported(HttpMediaTypeNotSupportedException e) {
        return BaseResponse.failed(ResultCode.MEDIA_TYPE_NOT_SUPPORTED);
    }

    @ExceptionHandler(Exception.class)
    public BaseResponse<Void> handleUnknownException(Exception e) {
        log.error("Unexpected exception: ", e);
        return BaseResponse.failed(ResultCode.INTERNAL_SERVER_ERROR);
    }

}
