package fr.smartsoft.sz.ai.assistant.rag.ingestion.impl;

import fr.smartsoft.sz.ai.assistant.rag.EmbeddingProvider;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.ChunkIndexer;
import fr.smartsoft.sz.ai.assistant.rag.ingestion.ChunkedDocument;
import fr.smartsoft.sz.ai.assistant.rag.pgvector.PgVectorFormat;
import fr.smartsoft.sz.ai.assistant.support.Exceptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Locale;

@Component
@Profile("rag-pgvector")
@Slf4j
public class PgChunkIndexer implements ChunkIndexer {

    private final DatabaseClient databaseClient;
    private final EmbeddingProvider embeddingProvider;

    public PgChunkIndexer(DatabaseClient databaseClient, EmbeddingProvider embeddingProvider) {
        this.databaseClient = databaseClient;
        this.embeddingProvider = embeddingProvider;
    }

    @Override
    public Mono<Void> index(ChunkedDocument chunk) {
        if (chunk == null || !StringUtils.hasText(chunk.content())) {
            return Mono.empty();
        }
        String sql = """
                INSERT INTO rag_chunks(
                    doc_id,
                    doc_title,
                    chunk_id,
                    content,
                    embedding,
                    source_type,
                    source_url
                )
                VALUES(
                    :docId,
                    :docTitle,
                    :chunkId,
                    :content,
                    CAST(:embedding AS vector),
                    :sourceType,
                    :sourceUrl
                )
                ON CONFLICT (chunk_id) DO UPDATE SET
                    doc_id = EXCLUDED.doc_id,
                    doc_title = EXCLUDED.doc_title,
                    content = EXCLUDED.content,
                    embedding = EXCLUDED.embedding,
                    source_type = EXCLUDED.source_type,
                    source_url = EXCLUDED.source_url
                """;
        return embeddingProvider.embed(chunk.content())
                .map(PgVectorFormat::toLiteral)
                .flatMap(embeddingLiteral -> databaseClient.sql(sql)
                        .bind("docId", chunk.docId())
                        .bind("docTitle", chunk.docTitle())
                        .bind("chunkId", chunk.chunkId())
                        .bind("content", chunk.content())
                        .bind("embedding", embeddingLiteral)
                        .bind("sourceType", chunk.sourceType().name())
                        .bind("sourceUrl", chunk.sourceUrl())
                        .fetch()
                        .rowsUpdated())
                .doOnError(ex -> {
                    log.error(
                            "Failed to index chunk docId={}, chunkId={}, rootCause={}",
                            chunk.docId(),
                            chunk.chunkId(),
                            Exceptions.rootMessage(ex),
                            ex
                    );
                })
                .onErrorMap(ex -> new IllegalStateException(
                        "failed to index chunk docId=" + chunk.docId() + ", chunkId=" + chunk.chunkId(),
                        ex
                ))
                .then();
    }

    private static Throwable rootCause(Throwable t) {
        Throwable result = t;
        while (result.getCause() != null) {
            result = result.getCause();
        }
        return result;
    }


}
