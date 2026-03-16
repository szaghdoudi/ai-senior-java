package fr.smartsoft.sz.ai.assistant.rag.ingestion;

import fr.smartsoft.sz.ai.assistant.rag.SourceType;

public record ChunkedDocument(
        String docId,
        String docTitle,
        String chunkId,
        String content,
        SourceType sourceType,
        String sourceUrl
) {
}
