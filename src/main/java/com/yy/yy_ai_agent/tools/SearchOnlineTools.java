package com.yy.yy_ai_agent.tools;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * @author 阿狸
 * @date 2026-02-08
 */
public class SearchOnlineTools {

    private final String API_KEY;

    public SearchOnlineTools(String apiKey) {
        this.API_KEY = apiKey;
    }

    @Tool(description = "search for information from baidu Search Engine")
    public String searchOnline(@ToolParam(description = "Search query keyword") String query) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("engine", "baidu");
        map.put("q", query);
        map.put("api_key", API_KEY);

        try {
            String response = HttpUtil.get("https://www.searchapi.io/api/v1/search", map);

            JSONObject jsonObject = JSONUtil.parseObj(response);

//            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
//
//            return organicResults.stream().map(obj -> {
//                JSONObject item = (JSONObject) obj;
//                JSONObject result = new JSONObject();
//                result.set("title", item.getStr("title"));
//                result.set("link", item.getStr("link"));
//                result.set("snippet", item.getStr("snippet"));
//                return result.toString();
//            }).collect(Collectors.joining(","));

            return jsonObject.toString();
        } catch (Exception e) {
            return "搜索失败：" + e.getMessage();
        }

    }
}
