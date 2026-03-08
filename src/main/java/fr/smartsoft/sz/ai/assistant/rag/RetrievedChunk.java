package fr.smartsoft.sz.ai.assistant.rag;

public record RetrievedChunk(
        String docId,
        String docTitle,
        String chunkId,
        String content,
        double score,
        SourceType sourceType,
        String sourceUrl
) {
}
