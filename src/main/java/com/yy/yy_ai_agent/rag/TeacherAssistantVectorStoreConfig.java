package com.yy.yy_ai_agent.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author 阿狸
 * @date 2026-02-09
 */
@Configuration
public class TeacherAssistantVectorStoreConfig {

    @Resource
    private TeacherAssistantAppDocumentLoader loader;

    @Bean
    VectorStore loadTAVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore vectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();

        List<Document> documents = loader.loadMarkdowns();

        vectorStore.add(documents);
        return vectorStore;
    }
}
