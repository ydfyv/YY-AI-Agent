package com.yy.yy_ai_agent.agent;

import com.yy.yy_ai_agent.agent.model.ThinkContext;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 * ReActAgent (Reasoning and Acting) 模式的代理抽象类
 * 实现了思考 - 行动的循环模式
 * @author 阿狸
 * @date 2026/2/13
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public abstract class ReActAgent extends BaseAgent {

    /**
     * 处理当前状态并决定下一步行动
     * @return 是否需要执行行动
     */
    public abstract ThinkContext think();

    /**
     * 执行行动
     * @return 执行的结果
     */
    public abstract String act();

    /**
     * 执行单个步骤： 思考和行动
     * @return 步骤执行结果
     */
    @Override
    public String step() {
        try {
            ThinkContext think = think();
            if (!think.getShouldAct()) {
                // 只需展示AI的思考结果给用户，无需将工具的调用结果给用户
//                "思考完成，无需行动: "
                return think.getThickResult();
            }
//            执行结果：" + act()
            return think.getThickResult();
        } catch (Exception e) {
            // 记录异常日志
            log.error("执行步骤发生异常", e);
            return "执行步骤发生异常" + e.getMessage();
        }
    }
}
