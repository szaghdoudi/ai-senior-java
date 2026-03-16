package fr.smartsoft.sz.ai.assistant.rag.ingestion;

import reactor.core.publisher.Flux;

public interface DocumentLoader {
    Flux<RagDocument> loadAll();
}
