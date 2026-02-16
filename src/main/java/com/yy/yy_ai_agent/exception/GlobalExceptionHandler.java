package com.yy.yy_ai_agent.exception;

import com.yy.yy_ai_agent.common.ErrorCode;
import com.yy.yy_ai_agent.common.Result;
import com.yy.yy_ai_agent.common.ResultUtils;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理类
 * 用于统一处理系统中出现的异常，提供统一的错误响应格式
 *
 * @author 阿狸
 * @date 2026/02/13
 */
@Slf4j
@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public Result<?> handleException(Exception e) {
        log.error("RuntimeException", e);
        return ResultUtils.error(ErrorCode.ERROR, e.getMessage());
    }

    @ExceptionHandler(BusinessException.class)
    public Result<?> handleBusinessException(BusinessException e) {
        log.error("BusinessException", e);
        return ResultUtils.error(e);
    }

}
