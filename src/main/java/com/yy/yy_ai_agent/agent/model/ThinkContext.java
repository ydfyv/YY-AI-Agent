package com.yy.yy_ai_agent.agent.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 定义思考上下文信息
 * @author 阿狸
 * @date 2026/02/20
 */
@Data
@AllArgsConstructor
public class ThinkContext {

    private Boolean shouldAct;

    private String thickResult;
}
