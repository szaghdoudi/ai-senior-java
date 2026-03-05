package fr.smartsoft.sz.ai.assistant.llm.dto;

public record ResolvedOptions(
        boolean ragUsed,
        int topK,
        double temperature,
        int maxTokens
) {}