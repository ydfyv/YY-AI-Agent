package com.yy.yy_ai_agent.common;

import com.yy.yy_ai_agent.exception.BusinessException;

/**
 * @author 阿狸
 * @date 2026/02/13
 */
public class ResultUtils {

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(ErrorCode.SUCCESS.getCode());
        result.setData(data);
        result.setMessage(ErrorCode.SUCCESS.getMessage());
        return result;
    }

    public static <T> Result<T> error(ErrorCode errorCode) {
        Result<T> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getMessage());
        return result;
    }

    public static <T> Result<T> error(ErrorCode errorCode, String message) {
        Result<T> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMessage(message);
        return result;
    }

    public static <T> Result<T> error(String message) {
        Result<T> result = new Result<>();
        result.setCode(ErrorCode.ERROR.getCode());
        result.setMessage(ErrorCode.ERROR.getMessage());
        return result;
    }

    public static <T> Result<T> error(BusinessException e) {
        Result<T> result = new Result<>();
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());
        return result;
    }
}
