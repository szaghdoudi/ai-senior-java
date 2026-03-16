package fr.smartsoft.sz.ai.assistant.rag.ingestion.impl;

import fr.smartsoft.sz.ai.assistant.rag.ingestion.ChunkIndexer;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.DocumentLoader;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.RagIngestionService;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.TextChunker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class DefaultRagIngestionService implements RagIngestionService {

    private final DocumentLoader documentLoader;
    private final TextChunker textChunker;
    private final ChunkIndexer chunkIndexer;

    public DefaultRagIngestionService(DocumentLoader documentLoader, TextChunker textChunker, ChunkIndexer chunkIndexer) {
        this.documentLoader = documentLoader;
        this.textChunker = textChunker;
        this.chunkIndexer = chunkIndexer;
    }

    @Override
    public Mono<Integer> ingestAll() {
        return documentLoader.loadAll()
                .flatMapIterable(textChunker::chunk)
                .flatMap(chunk -> chunkIndexer.index(chunk).thenReturn(1))
                .reduce(0,Integer::sum);
    }
}
