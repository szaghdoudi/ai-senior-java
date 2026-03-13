package fr.smartsoft.sz.ai.assistant.rag;

import reactor.core.publisher.Mono;

import java.util.List;

public interface VectorStore {
    Mono<List<VectorSearchMatch>> search(float[] queryEmbedding, int topK);
}
