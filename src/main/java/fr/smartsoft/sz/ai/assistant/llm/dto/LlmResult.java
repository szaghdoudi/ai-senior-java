package fr.smartsoft.sz.ai.assistant.llm.dto;

public record LlmResult(
        String provider,
        String model,
        String content
) {}