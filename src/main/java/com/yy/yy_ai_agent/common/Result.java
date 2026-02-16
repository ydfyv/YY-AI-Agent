package com.yy.yy_ai_agent.common;

import lombok.Data;

/**
 * @author 阿狸
 * @date 2026/02/13
 */
@Data
public class Result<T> {

    private Integer code;

    private String message;

    private T data;
}
