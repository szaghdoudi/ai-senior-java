package fr.smartsoft.sz.ai.assistant.dto;

public record AiAskRequest(
        String question,
        Context context,
        Options options
) {
    public record Context(String project, String service, String environment) {
    }

    public record Options(
            Boolean useRag,
            Integer topK,
            Double temperature,
            Integer maxTokens
    ) {
    }
}