package com.network.common.base;

import com.network.common.constant.ResultCode;
import lombok.Data;

import java.io.Serializable;

@Data
public class BaseResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private T data;

    private BaseResponse() {}

    public static <T> BaseResponse<T> success(T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.code = ResultCode.SUCCESS.getCode();
        response.message = ResultCode.SUCCESS.getMessage();
        response.data = data;
        return response;
    }

    public static <T> BaseResponse<T> success() {
        return success(null);
    }

    public static <T> BaseResponse<T> success(String message, T data) {
        BaseResponse<T> response = new BaseResponse<>();
        response.code = ResultCode.SUCCESS.getCode();
        response.message = message;
        response.data = data;
        return response;
    }

    public static <T> BaseResponse<T> failed(int code, String message) {
        BaseResponse<T> response = new BaseResponse<>();
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T> BaseResponse<T> failed(ResultCode resultCode) {
        return failed(resultCode.getCode(), resultCode.getMessage());
    }

    public static <T> BaseResponse<T> failed(String message) {
        return failed(ResultCode.FAILED.getCode(), message);
    }

}
