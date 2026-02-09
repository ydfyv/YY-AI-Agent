package com.yy.yy_ai_agent.rag;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.markdown.MarkdownDocumentReader;
import org.springframework.ai.reader.markdown.config.MarkdownDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 阿狸
 * @date 2026-02-09
 */
@Slf4j
@Component
public class TeacherAssistantAppDocumentLoader {

    private final ResourcePatternResolver resourcePatternResolver;

    public TeacherAssistantAppDocumentLoader(ResourcePatternResolver resourcePatternResolver) {
        this.resourcePatternResolver = resourcePatternResolver;
    }

    public List<Document> loadMarkdowns() {
        List<Document> documents = new ArrayList<>();

        try {
            Resource[] resources = resourcePatternResolver.getResources("classpath*:document/*.md");
            for (Resource resource : resources) {
                String filename = resource.getFilename();

                MarkdownDocumentReaderConfig config = MarkdownDocumentReaderConfig.builder().withHorizontalRuleCreateDocument(true)
                        .withIncludeBlockquote(false)
                        .withIncludeCodeBlock(false)
                        .withAdditionalMetadata("filename", filename)
                        .withAdditionalMetadata("grade", getGrade(filename))
                        .build();

                MarkdownDocumentReader reader = new MarkdownDocumentReader(resource, config);

                documents.addAll(reader.get());
            }
        } catch (IOException e) {
            log.error("Markdown 文档加载失败", e);
        }

        return documents;
    }

    public String getGrade(String filename) {
        return filename.substring(filename.lastIndexOf("-") + 1, filename.lastIndexOf("."));
    }
}
