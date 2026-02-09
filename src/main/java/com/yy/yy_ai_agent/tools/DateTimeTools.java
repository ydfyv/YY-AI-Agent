package com.yy.yy_ai_agent.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeTools {

    @Tool(description = "获取当前时间")
    public String getCurrentDateTime() {
        return LocalDateTime.now().atZone(LocaleContextHolder.getTimeZone().toZoneId()).toString();
    }

    @Tool(description = "为用户设置一个闹钟，参数 time 格式应为 ISO_LOCAL_DATE_TIME，例如 '2026-02-03T15:30'")
    public String setAlarm(String time) {
        try {
            LocalDateTime alarmTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            System.out.println("闹钟已设置为: " + alarmTime);
            // TODO: 实际闹钟逻辑（如调度、通知等）
            return "闹钟已设置为: " + alarmTime;
        } catch (Exception e) {
            return "时间格式无效，请使用 ISO_LOCAL_DATE_TIME 格式，如 '2026-02-03T15:30'";
        }
    }
}