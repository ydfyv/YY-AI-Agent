package com.yy.yy_ai_agent.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class YYManusTest {

    @Resource
    private YYManus yyManus;

    @Test
    public void test() {
//       String userPrompt = """
//               请帮我搜索一份网络上收藏最多的Java后端开发社招的简历，
//               并以 markdown 格式输出在项目根目录路径下的 [Java.md] 文件中
//               """;
//        String answer = yyManus.run(userPrompt);
//        Assertions.assertNotNull(answer);
    }

}