package com.yy.yy_ai_agent.common;

/**
 * 错误码
 * @author 阿狸
 * @date 2026/02/13
 */
public enum ErrorCode {

    SUCCESS(20000, "成功"),
    ERROR(50000, "系统错误"),
    PARAM_ERROR(40000, "参数错误"),
    NOT_FOUND(40400, "未找到"),
    UNAUTHORIZED(40100, "未授权"),
    FORBIDDEN(40300, "禁止访问"),
    AI_OVER_MAX_STEPS(40200, "AI调用超过最大步数"),
    AI_OVER_MAX_TOKENS(40201, "AI调用超过最大token数"),
    AI_OVER_MAX_TIME(40202, "AI调用超过最大时间"),
    AI_INTERNAL_ERROR(40203, "AI内部错误"),
    AI_INVOKE_STATE_ERROR(40204, "AI调用状态错误"),
    ;

    private final Integer code;

    private final String msg;

    ErrorCode(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return msg;
    }
}
