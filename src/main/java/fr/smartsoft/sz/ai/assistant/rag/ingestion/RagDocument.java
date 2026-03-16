package fr.smartsoft.sz.ai.assistant.rag.ingestion;

import fr.smartsoft.sz.ai.assistant.rag.SourceType;

public record RagDocument(
        String docId,
        String title,
        String content,
        SourceType sourceType,
        String sourceUrl
) {}
