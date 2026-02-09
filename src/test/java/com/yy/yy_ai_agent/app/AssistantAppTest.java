package com.yy.yy_ai_agent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
class AssistantAppTest {

    @Resource
    private AssistantApp assistantApp;

    @Test
    void doChat() {
        UUID uuid = UUID.randomUUID();
        String response = assistantApp.doChat("李白是谁？把他的信息用markdown格式写在项目根目录下的 [李白.md] 中", uuid.toString());
        System.out.println(response);
        Assertions.assertNotNull(response);
    }

    @Test
    void doChat2() {
        UUID uuid = UUID.randomUUID();
        String response = assistantApp.doChat("在终端中执行：dir /b > 文件.txt，将当前目录下所有文件和文件夹的名称以简洁格式输出到同目录下的 文件.txt 中, 编码是utf-8", uuid.toString());
        System.out.println(response);
        Assertions.assertNotNull(response);
    }
}