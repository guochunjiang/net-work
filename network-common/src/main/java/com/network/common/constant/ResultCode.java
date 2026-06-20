package com.network.common.constant;

import lombok.Getter;

@Getter
public enum ResultCode {

    SUCCESS(200, "Success"),
    FAILED(400, "Request failed"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Resource not found"),
    VALIDATION_FAILED(422, "Parameter validation failed"),
    METHOD_NOT_SUPPORTED(405, "HTTP method not supported"),
    MEDIA_TYPE_NOT_SUPPORTED(415, "Media type not supported"),
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    SERVICE_UNAVAILABLE(503, "Service unavailable"),
    BUSINESS_ERROR(1001, "Business logic error"),
    DATA_ALREADY_EXISTS(1002, "Data already exists"),
    DATA_NOT_FOUND(1003, "Data not found"),

    ;

    private final int code;
    private final String message;

    ResultCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

}
