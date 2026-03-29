package fr.smartsoft.sz.ai.assistant.rag;

import java.util.List;

public interface PromptBuilder {
    String build(String question, List<RetrievedChunk> chunks);
}