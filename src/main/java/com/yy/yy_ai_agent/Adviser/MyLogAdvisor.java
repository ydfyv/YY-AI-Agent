package com.yy.yy_ai_agent.Adviser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;

/**
 * @author 阿狸
 * @date 2026-02-03
 */
@Slf4j
public class MyLogAdvisor implements BaseAdvisor {

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        logRequest(chatClientRequest);

        return chatClientRequest;
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        logResponse(chatClientResponse);
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return 5000;
    }

    private void logRequest(ChatClientRequest request) {
        log.info("request: {}", request);
    }

    private void logResponse(ChatClientResponse chatClientResponse) {
        log.info("response: {}", chatClientResponse);
    }

}
