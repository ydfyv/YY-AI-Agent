package com.yy.yy_ai_agent.app;

import com.yy.yy_ai_agent.Adviser.MyLogAdvisor;
import com.yy.yy_ai_agent.chatMemory.FileBaseChatMemory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * @author 阿狸
 * @date 2026-02-08
 */
@Component
public class AssistantApp {

    private final ChatClient chatClient;

    public AssistantApp(ChatClient.Builder builder, ToolCallback[] toolCallbacks) {
        // 基于本地文件记忆的chatClient
        String fileDir = System.getProperty("user.dir") + "/chat-memory/Assistant";
        ChatMemory chatMemory = new FileBaseChatMemory(fileDir);
        this.chatClient = builder
                .defaultSystem("你是一个小助手")
                .defaultAdvisors(
                        new MyLogAdvisor(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultToolCallbacks(toolCallbacks)
                .build();
    }

    public String doChat(String prompt, String chatId) {
        return chatClient.prompt().user(prompt)
                .advisors(advisorSpec -> advisorSpec.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content();
    }
}
