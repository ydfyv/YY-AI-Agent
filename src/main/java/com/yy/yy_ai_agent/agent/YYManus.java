package com.yy.yy_ai_agent.agent;

import com.yy.yy_ai_agent.adviser.MyLogAdvisor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;

/**
 * @author 阿狸
 * @date 2026/2/13
 */
@Component
public class YYManus extends ToolCallAgent {

    public YYManus(ToolCallback[] availableTools, ChatModel dashscopeChatModel) {
        super(availableTools);
        this.setName("YY Manus");

        String SYSTEM_PROMPT = """
               You are YYManus, an all-capable AI assistant, aimed at solving any task presented by the user. You have various tools at your disposal that you can call upon to efficiently complete complex requests. Whether it's programming, information retrieval, file processing, web browsing, or human interaction (only for extreme cases), you can handle it all.
               The initial directory is:
               """ + System.getProperty("user.dir") + "\n";
        this.setSystemPrompt(SYSTEM_PROMPT);

        String NEXT_STEP_PROMPT = """
                Based on user needs, proactively select the most appropriate tool or combination of tools. For complex tasks, you can break down the problem and use different tools step by step to solve it. After using each tool, clearly explain the execution results and suggest the next steps.
                
                If you want to stop the interaction at any point, use the `terminate` tool/function call.
                """;
        this.setNextStepPrompt(NEXT_STEP_PROMPT);

        this.setMaxSteps(20);
        // 初始化 AI 对话客户端
        ChatClient chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultAdvisors(new MyLogAdvisor())
                .build();
        this.setChatClient(chatClient);
    }
}
