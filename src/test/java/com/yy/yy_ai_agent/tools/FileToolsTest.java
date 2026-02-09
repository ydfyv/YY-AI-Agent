package com.yy.yy_ai_agent.tools;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

class FileToolsTest {

    private static final String FILE_PATH = System.getProperty("user.dir") + File.separator + "test.properties";

    @Test
    void readFile() {
        FileTools fileTools = new FileTools();
        String s = fileTools.readFile(FILE_PATH);
        Assertions.assertNotNull(s);
    }

    @Test
    void writeFile() {
        FileTools fileTools = new FileTools();
        String s = fileTools.writeFile(FILE_PATH, "test你真棒！");
        Assertions.assertNotNull(s);
    }
}