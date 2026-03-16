package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.SourceType;
import fr.smartsoft.sz.ai.assistant.rag.VectorSearchMatch;
import fr.smartsoft.sz.ai.assistant.rag.VectorStore;
import fr.smartsoft.sz.ai.assistant.rag.pgvector.PgVectorFormat;
import org.springframework.context.annotation.Profile;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;

@Component
@Profile("rag-pgvector")
public class PgVectorStore implements VectorStore {
    private final DatabaseClient databaseClient;

    public PgVectorStore(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Mono<List<VectorSearchMatch>> search(float[] queryEmbedding, int topK) {
        int safeTopK = Math.max(1, Math.min(topK, 10));
        String embeddingLiteral = PgVectorFormat.toLiteral(queryEmbedding);

        String sql = """
                SELECT
                    doc_id,
                    doc_title,
                    chunk_id,
                    content,
                    source_type,
                    source_url,
                    1 - (embedding <=> CAST(:embedding AS vector)) AS score
                FROM rag_chunks
                ORDER BY embedding <=> CAST(:embedding AS vector)
                LIMIT :topK
                """;
        return databaseClient.sql(sql)
                .bind("embedding", embeddingLiteral)
                .bind("topK", topK)
                .map((row, meta) -> new VectorSearchMatch(
                        row.get("doc_id", String.class),
                        row.get("doc_title", String.class),
                        row.get("chunk_id", String.class),
                        row.get("content", String.class),
                        nullableDouble(row.get("score", Number.class)),
                        parseSourceType(row.get("source_type", String.class)),
                        row.get("source_url", String.class)
                ))
                .all()
                .collectList();
    }

    private SourceType parseSourceType(String value) {
        if (!StringUtils.hasText(value)) {
            return SourceType.MARKDOWN;
        }
        return SourceType.valueOf(value.toUpperCase(Locale.ROOT));
    }

    private double nullableDouble(Number n) {
        return n == null ? 0.0 : n.doubleValue();
    }


}
