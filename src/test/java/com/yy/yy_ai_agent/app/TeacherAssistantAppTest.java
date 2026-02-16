package com.yy.yy_ai_agent.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherAssistantAppTest {

    @Resource
    private TeacherAssistantApp teacherAssistantApp;

    @Test
    void doChatWithRag() {
        UUID uuid = UUID.randomUUID();
        String s = teacherAssistantApp.doChatByRag("高三数学应该掌握什么知识, 用markdown格式写在项目根路径下的 【高三数学必学知识.md】", uuid.toString());
        Assertions.assertNotNull(s);
    }
}