package com.yy.yy_ai_agent.demo.invoke;

import com.yy.yy_ai_agent.Adviser.MyLogAdviser;
import com.yy.yy_ai_agent.Adviser.ReReadingAdviser;
import com.yy.yy_ai_agent.chatMemory.FileBaseChatMemory;
import com.yy.yy_ai_agent.tools.DateTimeTools;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class SpringAIInvoke implements CommandLineRunner {

//    @Resource
//    private ChatModel dashscopeChatModel;

    private final ChatClient chatClient;

    @Resource
    private ChatModel dashscopeChatModel;

    public SpringAIInvoke(ChatClient.Builder builder) {
//        ChatMemory chatMemory = MessageWindowChatMemory.builder().build();
        // 创建内存记忆的chatClient

        // 基于本地文件记忆的chatClient
        String fileDir = System.getProperty("user.dir") + "/chat-memory";
        ChatMemory chatMemory = new FileBaseChatMemory(fileDir);

//        this.chatClient = builder
//                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build(),
//                        new MyLogAdviser(),
//                        new ReReadingAdviser()
//                )
//                .defaultTools(new DateTimeTools())
//                .defaultSystem("你是一个程序员")
//                .build();

        this.chatClient = builder
                .defaultSystem("你是一个小助手")
                .defaultAdvisors(
                        new MyLogAdviser(),
                        new ReReadingAdviser(),
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(new DateTimeTools())
                .build();
    }

    @Override
    public void run(String... args) {
        String input1 = "你好，我是程序员阿狸。请介绍一下你自己";
        System.out.println("user:" + input1);
        String content = chatClient.prompt(input1).call().content();
        System.out.println("client:" + content);
        System.out.println("------------------------------------");

        String input2 = "我叫什么名字？刚才告诉过你。";
        System.out.println("user:" + input2);
        String content2 = chatClient.prompt(input2).call().content();
        System.out.println("client:" + content2);
        System.out.println("------------------------------------");

        String input3 = "明天是什么日子？";
        System.out.println("user:" + input3);
        String content3 = chatClient.prompt(input3).call().content();
        System.out.println("client:" + content3);
        System.out.println("------------------------------------");

        String input4 = "帮我设置一个 10 分钟后的闹钟, 并告诉我闹钟的时间";
        System.out.println("user:" + input4);
        String content4 = chatClient.prompt(input4).call().content();
        System.out.println("client:" + content4);
        System.out.println("------------------------------------");

//        String input4 = "我叫什么名字";
//        System.out.println("user:" + input4);
//        String content4 = chatClient.prompt(input4).call().content();
//        System.out.println("client:" + content4);
//        System.out.println("------------------------------------");
    }

//    @Override
//    public void run(String... args) throws Exception {
//        Flux<ChatResponse> stream = dashscopeChatModel.stream(new Prompt("你好，我是程序员阿狸。请介绍一下你自己"));
//
//        stream.subscribe(assistantMessage -> {
//            AssistantMessage output = assistantMessage.getResult().getOutput();
//            System.out.print(output.getText());
//        });
//
//    }
}
