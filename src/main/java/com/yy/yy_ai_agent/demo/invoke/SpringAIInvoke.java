package com.yy.yy_ai_agent.demo.invoke;

import com.yy.yy_ai_agent.adviser.MyLogAdvisor;
import com.yy.yy_ai_agent.adviser.ReReadingAdviser;
import com.yy.yy_ai_agent.chatMemory.FileBaseChatMemory;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

@Component
public class SpringAIInvoke {

//    @Resource
//    private ChatModel dashscopeChatModel;

    private final ChatClient chatClient;

    @Resource
    private ChatModel dashscopeChatModel;


    public SpringAIInvoke(ChatClient.Builder builder, ToolCallback[] toolCallbacks) {
//        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        // 创建内存记忆的chatClient

        // 基于本地文件记忆的chatClient
        String fileDir = System.getProperty("user.dir") + "/chat-memory";
        ChatMemory chatMemory = new FileBaseChatMemory(fileDir);

//        this.chatClient = builder
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
//                        new MyLogAdvisor(),
//                        new ReReadingAdviser()
//                )
//                .defaultTools(new DateTimeTools())
//                .defaultSystem("你是一个程序员")
//                .build();

        this.chatClient = builder
                .defaultSystem("你是一个小助手")
                .defaultAdvisors(
                        new MyLogAdvisor(),
                        new ReReadingAdviser(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
//                .defaultTools(new DateTimeTools())
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
