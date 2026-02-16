package com.yy.yy_ai_agent.controller;

import com.yy.yy_ai_agent.agent.YYManus;
import com.yy.yy_ai_agent.app.TeacherAssistantApp;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;

/**
 * @author 阿狸
 * @date 2026/02/14
 */
@RestController
@RequestMapping("/ai")
public class AiController {

    @Resource
    private TeacherAssistantApp teacherAssistantApp;

    @Resource
    private ToolCallback[] availableTools;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 同步调用
     *
     * @return 响应结果
     */
    @GetMapping("/teacher-assistant/chat/sync")
    public String teacherAssistantAppSync(@RequestParam("prompt") String prompt, @RequestParam("chatId") String chatId) {
        return teacherAssistantApp.doChatBySync(prompt, chatId);
    }

    /**
     * SSE 流式调用
     *
     * @return 响应结果
     */
    @GetMapping(value = "/teacher-assistant/chat/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> teacherAssistantAppSse(@RequestParam("prompt") String prompt, @RequestParam("chatId") String chatId) {
        return teacherAssistantApp.doChatByStream(prompt, chatId);
    }

    /**
     * SSE 流式调用
     *
     * @return 响应结果
     */
    @GetMapping(value = "/teacher-assistant/chat/server-sent-event")
    public Flux<ServerSentEvent<String>> teacherAssistantAppServerSentEvent(@RequestParam("prompt") String prompt, @RequestParam("chatId") String chatId) {
        return teacherAssistantApp.doChatByStream(prompt, chatId)
                .map(chunk -> ServerSentEvent.<String>builder().data(chunk).build());
    }

    /**
     * SSE 流式调用
     *
     * @return 响应结果
     */
    @GetMapping(value = "/teacher-assistant/chat/sse-emitter")
    public SseEmitter teacherAssistantAppSseEmitter(@RequestParam("prompt") String prompt, @RequestParam("chatId") String chatId) {
        SseEmitter sseEmitter = new SseEmitter(180000L); // 3分钟超时时间
        // 获取 Flux 响应式数据流并且直接通过订阅推送给 SseEmitter
        teacherAssistantApp.doChatWithRagByStream(prompt, chatId)
                .subscribe(chunk -> {
                    try {
                        sseEmitter.send(chunk);
                    } catch (IOException e) {
                        sseEmitter.completeWithError(e);
                    }
                }, sseEmitter::completeWithError, sseEmitter::complete);
        return sseEmitter;
    }

    /**
     * 流式调用 manus 智能体
     * @param prompt 提示词
     * @return sseEmitter
     */
    @GetMapping("/chat/manus")
    public SseEmitter yymanusSse(@RequestParam("prompt") String prompt) {
        YYManus yyManus = new YYManus(availableTools, dashscopeChatModel);
        return yyManus.runStream(prompt);
    }
}
