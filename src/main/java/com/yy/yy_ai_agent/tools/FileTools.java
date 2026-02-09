package com.yy.yy_ai_agent.tools;


import cn.hutool.core.io.file.FileReader;
import cn.hutool.core.io.file.FileWriter;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * @author 阿狸
 * @date 2026-02-08
 */
public class FileTools {


    @Tool(description = "read a file from given path")
    public String readFile(@ToolParam(description = "path of being read file") String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return "文件路径不能为空";
        }

        try{
            FileReader fileReader = new FileReader(filePath);
            return fileReader.readString();
        } catch (Exception e) {
            return "文件读取失败：" + e.getMessage();
        }
    }

    @Tool(description = "write a file from given path")
    public String writeFile(@ToolParam(description = "path of being written file") String filePath,
                            @ToolParam(description = "being written content") String content) {
        if (filePath == null || filePath.isEmpty()) {
            return "文件路径不能为空";
        }

        try {
            FileWriter writer = new FileWriter(filePath);
            writer.write(content);
            return "文件写入成功";
        } catch (Exception e) {
            return "文件写入失败：" + e.getMessage();
        }
    }
}
