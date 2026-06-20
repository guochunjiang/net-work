package com.network.common.exception;

import com.network.common.constant.ResultCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final int code;

    public GlobalException(String message) {
        super(message);
        this.code = ResultCode.BUSINESS_ERROR.getCode();
    }

    public GlobalException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    public GlobalException(int code, String message) {
        super(message);
        this.code = code;
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
        this.code = ResultCode.INTERNAL_SERVER_ERROR.getCode();
    }

}
