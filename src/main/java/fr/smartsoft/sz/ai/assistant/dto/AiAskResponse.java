package fr.smartsoft.sz.ai.assistant.dto;

import java.util.List;
import java.util.Map;

public record AiAskResponse(
        String answer,
        List<Citation> citations,
        Meta meta
) {
    public record Citation(
            String docId,
            String docTitle,
            String chunkId,
            Double score,
            SourceType sourceType,
            String sourceUrl
    ) {}

    public enum SourceType { CONFLUENCE, GIT, PDF, MARKDOWN, LOG }

    public record Meta(
            String requestId,
            String model,
            String provider,
            Boolean ragUsed,
            Retrieval retrieval,
            Map<String, Long> timingsMs,
            Cost cost
    ) {
        public record Retrieval(Integer topK) {}
        public record Cost(Integer inputTokens, Integer outputTokens, Integer totalTokens, Double estimatedUsd) {}
    }
}