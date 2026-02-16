package com.yy.yy_ai_agent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * @author 阿狸
 * @date 2026-02-08
 */
public class CommonTools {

    @Tool(description = "获取项目根目录")
    public String getProjectPath(){
        return System.getProperty("user.dir");
    }
}
