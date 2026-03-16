package fr.smartsoft.sz.ai.assistant.rag.ingestion;

import java.util.List;

public interface TextChunker {
    List<ChunkedDocument> chunk(RagDocument document);
}
