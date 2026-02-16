package com.yy.yy_ai_agent.chat;

import com.yy.yy_ai_agent.demo.invoke.SpringAIInvoke;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

/**
 * @author 阿狸
 * @date 2026-02-08
 */
@SpringBootTest
public class AIApp {

    @Resource
    private SpringAIInvoke springAIInvoke;

    @Test
    public void test() {
//        UUID uuid = UUID.randomUUID();
//        String response = springAIInvoke.doChat("李白是谁？给我一个网址", uuid.toString());
//        System.out.println(response);
//        Assertions.assertNotNull(response);
    }
}
