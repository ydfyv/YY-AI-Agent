package com.yy.yy_ai_agent.demo.invoke;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
public class SpringAIInvoke implements CommandLineRunner {

    @Resource
    private ChatModel dashscopeChatModel;

    @Override
    public void run(String... args) throws Exception {
        Flux<ChatResponse> stream = dashscopeChatModel.stream(new Prompt("你好，我是程序员阿狸。请介绍一下你自己"));

        stream.subscribe(assistantMessage -> {
            AssistantMessage output = assistantMessage.getResult().getOutput();
            System.out.print(output.getText());
        });

    }
}
