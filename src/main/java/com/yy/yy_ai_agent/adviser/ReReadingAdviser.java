package com.yy.yy_ai_agent.adviser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;

import java.util.Map;

/**
 * @author 阿狸
 * @date 2026-02-07
 */
@Slf4j
public class ReReadingAdviser implements BaseAdvisor {

    private static final String DEFAULT_RE2_ADVISE_TEMPLATE = """
			{re2_input_query}
			Read the question again: {re2_input_query}
			""";


    private final String re2AdviseTemplate;

    private int order = 0;


    public ReReadingAdviser() {
        this(DEFAULT_RE2_ADVISE_TEMPLATE);
    }

    public ReReadingAdviser(String defaultRe2AdviseTemplate) {
        this.re2AdviseTemplate = defaultRe2AdviseTemplate;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        String augmentedUserText = PromptTemplate.builder()
                .template(this.re2AdviseTemplate)
                .variables(Map.of("re2_input_query", chatClientRequest.prompt().getUserMessage().getText()))
                .build()
                .render();

        log.info("ReReadingAdviser augment user message: {}", augmentedUserText);

        return chatClientRequest.mutate()
                .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
                .build();
    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        return chatClientResponse;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    public ReReadingAdviser withOrder(int order) {
        this.order = order;
        return this;
    }
}
