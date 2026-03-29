package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.PromptBuilder;
import fr.smartsoft.sz.ai.assistant.rag.RetrievedChunk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class ResourcePromptBuilder implements PromptBuilder {

    private final String withRagTemplate;
    private final String withoutRagTemplate;

    public ResourcePromptBuilder(
            @Value("classpath:prompts/rag/ask-with-rag.txt") Resource withRag,
            @Value("classpath:prompts/rag/ask-without-rag.txt") Resource withoutRag
    ) throws IOException {
        this.withRagTemplate = new String(withRag.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        this.withoutRagTemplate = new String(withoutRag.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    @Override
    public String build(String question, List<RetrievedChunk> chunks) {
        if (chunks == null || chunks.isEmpty()) {
            return withoutRagTemplate.replace("{{question}}", question);
        }

        StringBuilder context = new StringBuilder();
        for (RetrievedChunk c : chunks) {
            context.append("- [docId=").append(c.docId())
                    .append(", chunkId=").append(c.chunkId())
                    .append(", score=").append(String.format("%.3f", c.score()))
                    .append("] ")
                    .append(c.content())
                    .append("\n");
        }

        return withRagTemplate
                .replace("{{context}}", context.toString())
                .replace("{{question}}", question);
    }
}