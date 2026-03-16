package fr.smartsoft.sz.ai.assistant.rag.ingestion;

import reactor.core.publisher.Mono;

public interface ChunkIndexer {
    Mono<Void> index(ChunkedDocument chunk);
}
