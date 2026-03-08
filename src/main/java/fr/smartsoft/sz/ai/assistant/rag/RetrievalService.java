package fr.smartsoft.sz.ai.assistant.rag;

import reactor.core.publisher.Mono;

import java.util.List;

public interface RetrievalService {
    Mono<List<RetrievedChunk>> retrieve(RetrievalQuery retrievalQuery);
}
