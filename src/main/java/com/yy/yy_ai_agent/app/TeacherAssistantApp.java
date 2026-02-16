package com.yy.yy_ai_agent.app;

import com.yy.yy_ai_agent.Adviser.MyLogAdvisor;
import com.yy.yy_ai_agent.chatMemory.FileBaseChatMemory;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author 阿狸
 * @date 2026-02-08
 */
@Component
public class TeacherAssistantApp {

    private final ChatClient chatClient;

    private static final String fileDir = System.getProperty("user.dir") + "/chat-memory/Teacher-Assistant";

    public static final String SYSTEM_PROMPT =
            """
                    你是一位专业的教师小助手，专注于为中国基础教育阶段（小学、初中、高中）提供数学学科教学支持。你熟悉《义务教育数学课程标准》和《普通高中数学课程标准》，清楚各学段学生应掌握的核心知识、关键能力与学习目标。
                    当用户提问时，请根据问题所涉及的学段（小学 / 初中 / 高中）准确判断对应的知识范围，并以清晰、简洁、教育性的方式回应。你的回答应：
                    - 明确指出该知识点所属的年级或学段；
                    - 说明学生“在什么学习情境下”应“掌握什么内容”；
                    - 必要时可提供教学建议、常见误区或衔接说明；
                    - 避免超纲内容（如对小学生讲解导数，或对初中生要求掌握空间向量）；
                    - 语言亲切、专业，符合教师身份。
                    你不是通用聊天机器人，而是聚焦于基础教育数学教学的专业辅助工具。
            """;

    @Resource
    private VectorStore loadTAVectorStore;

    public TeacherAssistantApp(ChatClient.Builder builder, ToolCallback[] toolCallbacks) {

        // 基于本地文件记忆的chatClient
        ChatMemory chatMemory = new FileBaseChatMemory(fileDir);
        this.chatClient = builder
                .defaultSystem(SYSTEM_PROMPT)
                .defaultAdvisors(
                        new MyLogAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultToolCallbacks(toolCallbacks)
                .build();
    }

    /**、
     * 同步调用AI大模型
     * @param prompt 提示词
     * @param chatId 会话Id
     * @return 响应结果
     */
    public String doChatBySync(String prompt, String chatId) {
        return chatClient.prompt().user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content();
    }

    /**
     * 同步调用AI大模型 (RAG)
     * @param prompt 提示词
     * @param chatId 会话Id
     * @return 响应结果
     */
    public String doChatByRag(String prompt, String chatId) {

        return chatClient
                .prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(QuestionAnswerAdvisor.builder(loadTAVectorStore).build(),
                        new MyLogAdvisor())
                .call()
                .content();
    }

    /**
     * 同步调用AI大模型 (RAG)
     * @param prompt 提示词
     * @param chatId 会话Id
     * @return 响应结果
     */
    public Flux<String> doChatWithRagByStream(String prompt, String chatId) {

        return chatClient
                .prompt()
                .user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .advisors(QuestionAnswerAdvisor.builder(loadTAVectorStore).build(),
                        new MyLogAdvisor())
                .stream()
                .content();
    }

    /**
     * 异步调用AI大模型 (流式调用)
     * @param prompt 提示词
     * @param chatId 会话Id
     * @return 响应结果
     */
    public Flux<String> doChatByStream(String prompt, String chatId) {
        return chatClient.prompt().user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .stream()
                .content();
    }
}
