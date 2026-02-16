package com.yy.yy_ai_agent.agent;

import cn.hutool.core.util.StrUtil;
import com.yy.yy_ai_agent.agent.model.AgentState;
import com.yy.yy_ai_agent.common.ErrorCode;
import com.yy.yy_ai_agent.exception.ThrowUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * 抽象基础代理类，用于管理代理状态和执行流程
 *
 * 提供状态转换、内存管理和基于步骤的执行循环的基础功能
 * 子类必须实现step方法
 *
 * @author 阿狸
 * @date 2026/2/13
 */
@Data
@Slf4j
public abstract class BaseAgent {

    // 核心属性
    private String name;

    // 提示词
    private String systemPrompt;
    private String nextStepPrompt;

    // 代理状态
    private AgentState state = AgentState.IDLE;

    // 执行步骤控制
    private Integer currentStep = 0;
    private Integer maxSteps = 10;

    // LLM 大模型
    private ChatClient chatClient;

    // Memory 记忆 （需要自主维护会话上下文）
    private List<Message> messageList = new ArrayList<>();

    /**
     * 运行代理
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public String run(String userPrompt) {
        // 基础校验
        ThrowUtils.throwIf(state != AgentState.IDLE,  ErrorCode.AI_INVOKE_STATE_ERROR ,"cannot run agent in state: " + this.state);

        ThrowUtils.throwIf(StrUtil.isBlank(userPrompt), ErrorCode.PARAM_ERROR, "userPrompt cannot be empty");

        // 执行，更改状态
        state = AgentState.RUNNING;
        // 记录消息上下文
        messageList.add(new UserMessage(userPrompt));
        // 保存结果列表
        List<String> results = new ArrayList<>();

        try {
            // 执行循环
            while (currentStep < this.maxSteps && this.state != AgentState.FINISHED) {
                currentStep++;
                log.info("Executing step: {} / {}", this.currentStep, maxSteps);

                // 执行单个步骤
                String stepResult = step();
                String result = "Step " + currentStep + ": " + stepResult;
                results.add(result);
            }

            // 检查是否超出步骤限制
            if (currentStep >= maxSteps) {
                state = AgentState.FINISHED;
                results.add("Terminated due to step limit [" + maxSteps + "] reached");
            }
            return String.join("\n", results);
        } catch (Exception e) {
            log.error("Agent run error", e);
            state = AgentState.ERROR;
            results.add("Error: " + e.getMessage());
            return "Error: " + e.getMessage();
        } finally {
            // 清理资源
            cleanup();
        }
    }

    /**
     * 运行代理 (流式输出)
     * @param userPrompt 用户提示词
     * @return 执行结果
     */
    public SseEmitter runStream(String userPrompt) {
        SseEmitter sseEmitter = new SseEmitter(300000L); // 5分钟超时时间

        CompletableFuture.runAsync(() -> {
            // 基础校验
            try {
                if (state != AgentState.IDLE) {
                    sseEmitter.send("错误，不能运行代理，状态为" + state);
                    sseEmitter.complete();
                    return;
                }

                if (StrUtil.isBlank(userPrompt)) {
                    sseEmitter.send("错误, 提示词不能为空" );
                    sseEmitter.complete();
                    return;
                }
            } catch (Exception exception) {
                sseEmitter.completeWithError(exception);
            }
            // 执行，更改状态
            state = AgentState.RUNNING;
            // 记录消息上下文
            messageList.add(new UserMessage(userPrompt));
            // 保存结果列表
            List<String> results = new ArrayList<>();

            try {
                // 执行循环
                while (currentStep < this.maxSteps && this.state != AgentState.FINISHED) {
                    currentStep++;
                    log.info("Executing step: {} / {}", this.currentStep, maxSteps);

                    // 执行单个步骤
                    String stepResult = step();
                    String result = "Step " + currentStep + ": " + stepResult;
                    results.add(result);

                    // 发送结果到SSE
                    sseEmitter.send(result);

                }

                // 检查是否超出步骤限制
                if (currentStep >= maxSteps) {
                    state = AgentState.FINISHED;
                    results.add("Terminated due to step limit [" + maxSteps + "] reached");

                    sseEmitter.send("Terminated due to step limit [" + maxSteps + "] reached");
                }

                // 正常完成
                sseEmitter.complete();

            } catch (Exception e) {
                log.error("Agent run error", e);
                state = AgentState.ERROR;
                results.add("Error: " + e.getMessage());
                try {
                    sseEmitter.send("Error: " + e.getMessage());
                    sseEmitter.complete();
                } catch (IOException ex) {
                    sseEmitter.completeWithError(ex);
                }

            } finally {
                // 清理资源
                cleanup();
            }
        });

        // 设置超时回调
        sseEmitter.onTimeout(() -> {
          this.state = AgentState.ERROR;
          this.cleanup();
          log.error("SSE connection timeout");
        });

        sseEmitter.onCompletion(() -> {
            if (state == AgentState.RUNNING) {
                state = AgentState.FINISHED;
            }
            this.cleanup();
            log.info("SSE connection completed");
        });

        sseEmitter.onError((e) -> {
            if (state == AgentState.RUNNING) {
                state = AgentState.ERROR;
            }
            this.cleanup();
            log.error("SSE connection error", e);
        });

        return sseEmitter;
    }

    /**
     * 定义单个步骤
     * @return 执行结果
     */
    public abstract String step();

    /**
     * 清理资源
     */
    protected void cleanup() {
        // 子类重写此方法清理资源
    }
}
