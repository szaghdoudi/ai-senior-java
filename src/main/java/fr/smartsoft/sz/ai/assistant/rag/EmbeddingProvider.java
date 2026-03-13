package fr.smartsoft.sz.ai.assistant.rag;

import reactor.core.publisher.Mono;

public interface EmbeddingProvider {
    Mono<float[]> embed(String text);
}
