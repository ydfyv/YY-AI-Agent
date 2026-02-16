package com.yy.yy_ai_agent.exception;

import com.yy.yy_ai_agent.common.ErrorCode;

/**
 * 抛异常工具类
 *
 * @author 阿狸
 * @date 2026/02/13
 */
public class ThrowUtils {

    public static void throwIf(boolean condition, ErrorCode errorCode) {
        if (condition)
            throw new BusinessException(errorCode);
    }

    public static void throwIf(boolean condition, RuntimeException exception) {
        if (condition) throw exception;
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        if (condition)
            throw new BusinessException(errorCode, message);
    }
}
