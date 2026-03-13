package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
@Service
public class DefaultRetrievalService implements RetrievalService {
    private final EmbeddingProvider embeddingProvider;
    private final VectorStore vectorStore;

    public DefaultRetrievalService(EmbeddingProvider embeddingProvider, VectorStore vectorStore) {
        this.embeddingProvider = embeddingProvider;
        this.vectorStore = vectorStore;
    }

    @Override
    public Mono<List<RetrievedChunk>> retrieve(RetrievalQuery retrievalQuery) {
        if (retrievalQuery == null || !StringUtils.hasText(retrievalQuery.query())) {
            return Mono.just(List.of());
        }

        int topK = Math.max(1, Math.min(retrievalQuery.topK(),10));

        return embeddingProvider.embed(retrievalQuery.query())
                .flatMap(embedding -> vectorStore.search(embedding, topK))
                .map(matchers -> matchers.stream()
                        .map(m -> new RetrievedChunk(
                                m.docId(),
                                m.docTitle(),
                                m.chunkId(),
                                m.content(),
                                m.score(),
                                m.sourceType(),
                                m.sourceUrl()
                        ))
                        .toList());

    }
}
