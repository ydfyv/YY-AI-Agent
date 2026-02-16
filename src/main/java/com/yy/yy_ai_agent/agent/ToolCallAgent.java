package com.yy.yy_ai_agent.agent;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatOptions;
import com.yy.yy_ai_agent.agent.model.AgentState;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.ai.tool.ToolCallback;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 处理工具调用的基础代理类，具体实现了 think 和 act 方法，可以用作创建实例的父类
 *
 * @author 阿狸
 * @date 2026/2/13
 */
@Slf4j
@EqualsAndHashCode(callSuper = true)
@Data
public class ToolCallAgent extends ReActAgent {

    // 可用的工具
    private final ToolCallback[] availableTools;

    // AI 返回结果
    private ChatResponse toolCallChatResponse;

    // 工具调用管理者
    private final ToolCallingManager toolCallingManager;

    // 禁用Spring AI 内置的工具调用机制，自己维护选项和上下文
    private final ChatOptions chatOptions;

    public ToolCallAgent(ToolCallback[] availableTools) {
        super();
        this.availableTools = availableTools;
        this.toolCallingManager = ToolCallingManager.builder().build();
//        this.chatOptions = ToolCallingChatOptions.builder().internalToolExecutionEnabled(false).build();
        this.chatOptions = DashScopeChatOptions.builder().internalToolExecutionEnabled(false).build();
    }

    /**
     * 处理当前状态，获取并决定下一步行动
     * @return
     */
    @Override
    public boolean think() {
        // 1.校验提示词，拼接用户提示词
        if (StrUtil.isNotBlank(getNextStepPrompt())) {
            UserMessage userMessage = new UserMessage(getNextStepPrompt());
            getMessageList().add(userMessage);
        }

        try {
            // 2.调用 AI 大模型，获取工具调用结果
            List<Message> messageList = getMessageList();

            Prompt prompt = new Prompt(messageList, chatOptions);
            toolCallChatResponse = getChatClient().prompt(prompt)
                    .system(getSystemPrompt())
                    .toolCallbacks(availableTools)
                    .call()
                    .chatResponse();
            // 记录响应，用于等下 Act
            // 3.解析工具调用结果，获取要调用的工具
            assert toolCallChatResponse != null;
            // 助手消息
            AssistantMessage assistantMessage = toolCallChatResponse.getResult().getOutput();
            String text = assistantMessage.getText();
            // 获取要调用的工具列表
            List<AssistantMessage.ToolCall> toolCalls = assistantMessage.getToolCalls();

            log.info(getName() + ": 思考结果：" + text);
            log.info(getName() + ": 选择了" + toolCalls.size() + "个工具");

            String toolCallInfos = toolCalls.stream()
                    .map(toolCall -> String.format("工具名称：%s，工具参数：%s", toolCall.name(), toolCall.arguments()))
                    .collect(Collectors.joining("\n"));
            log.info(getName() + ": 工具调用信息：" + toolCallInfos);

            if (toolCalls.isEmpty()) {
                // 只有不调用工具时， 才需要手动记录助手消息
                getMessageList().add(assistantMessage);
                return false;
            } else {
                // 需要调用工具时，无需记录助手消息，因为调用工具时，会自动记录
                return true;
            }
        } catch (Exception e) {
            log.error(getName() + "的思考过程遇到了问题" + e.getMessage());
            new AssistantMessage("处理时遇到了问题：" + e.getMessage());
            return false;
        }
    }

    /**
     * 执行工具调用并处理结果
     * @return 执行结果
     */
    @Override
    public String act() {
        try {
            if (!toolCallChatResponse.hasToolCalls()) {
                return "没有工具需要调用";
            }
            // 调用工具
            Prompt prompt = new Prompt(getMessageList(), chatOptions);
            ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(prompt, getToolCallChatResponse());
            // 记录消息上下文，conversationHistory 已经包含了助手消息和工具调用返回的结果
            setMessageList(toolExecutionResult.conversationHistory());
            ToolResponseMessage toolResponseMessage = (ToolResponseMessage) CollUtil.getLast(toolExecutionResult.conversationHistory());

            // 判断是否调用了终止工具
            if (toolResponseMessage.getResponses().stream().anyMatch(response -> response.name().equals("doTerminate"))) {
                // 任务结束，更改状态
                setState(AgentState.FINISHED);
            }

            String results = toolResponseMessage.getResponses().stream()
                    .map(response -> "工具" + response.name() + " 返回结果：" + response.responseData())
                    .collect(Collectors.joining("\n"));
            log.info(getName() + ": 工具调用结果：" + results);
            return results;
        } catch (Exception e) {
            log.error(getName() + "的执行过程遇到了问题" + e.getMessage());
            return "执行时遇到了问题：" + e.getMessage();
        }
    }
}
