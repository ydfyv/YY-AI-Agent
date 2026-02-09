package com.yy.yy_ai_agent.tools;

import org.springframework.ai.support.ToolCallbacks;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 阿狸
 * @date 2026-02-08
 */
@Configuration
public class ToolRegistration {

    @Value("${search-api.api-key}")
    private String apiKey;


    @Bean
    public ToolCallback[] searchOnlineTools() {
        DateTimeTools dateTimeTools = new DateTimeTools();
        FileTools fileTools = new FileTools();
        SearchOnlineTools searchOnlineTools = new SearchOnlineTools(apiKey);
        CommonTools commonTools = new CommonTools();
        TerminalOperationTool terminalOperationTool = new TerminalOperationTool();

        return ToolCallbacks.from(dateTimeTools, fileTools, commonTools, terminalOperationTool);
    }
}
