package com.yy.yy_ai_agent.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * 终止工具（目的是让自主规划AI智能体能够合理的终端）
 * @author 阿狸
 * @date 2026/02/13
 */
public class TerminateTools {

    @Tool(description = "Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.\n" +
            "When you have finished all the tasks, call this tool to end the work.")
    public String doTerminate() {
        return "任务结束";
    }
}
