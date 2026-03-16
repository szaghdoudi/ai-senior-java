package fr.smartsoft.sz.ai.assistant.rag.ingestion;

import reactor.core.publisher.Mono;

public interface RagIngestionService {
    Mono<Integer> ingestAll();
}
