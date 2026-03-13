package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.SourceType;
import fr.smartsoft.sz.ai.assistant.rag.VectorSearchMatch;
import fr.smartsoft.sz.ai.assistant.rag.VectorStore;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
@Component
@Profile("!rag-pgvector")
public class SimpleInMemoryVectorStore implements VectorStore {

    private static final List<VectorSearchMatch> KB = List.of(
            new VectorSearchMatch(
                    "rb-001",
                    "LLM provider down / timeouts",
                    "rb-001-c1",
                    "If latency increases, verify provider status, timeout settings, and retries.",
                    0.0,
                    SourceType.MARKDOWN,
                    "docs/runbooks/RB-001-llm-provider-down.md"
            ),
            new VectorSearchMatch(
                    "arch-runtime",
                    "Runtime defaults and guardrails",
                    "arch-runtime-c1",
                    "Default topK is 5, maxTokens is 800, and server-side guardrails clamp values.",
                    0.0,
                    SourceType.MARKDOWN,
                    "docs/architecture/runtime-defaults.md"
            ),
            new VectorSearchMatch(
                    "checkpoint-2026-03-05",
                    "Project checkpoint",
                    "checkpoint-c1",
                    "Current provider is ollama and latency is mostly from LLM call duration.",
                    0.0,
                    SourceType.MARKDOWN,
                    "docs/notes/checkpoint-2026-03-05.md"
            )
    );

    @Override
    public Mono<List<VectorSearchMatch>> search(float[] queryEmbedding, int topK) {
        int safeTopK = Math.max(1, Math.min(topK, 10));
        List<VectorSearchMatch> out = KB.stream()
                .map(m -> new VectorSearchMatch(
                        m.docId(),
                        m.docTitle(),
                        m.chunkId(),
                        m.content(),
                        pseudoScore(m.content(), queryEmbedding),
                        m.sourceType(),
                        m.sourceUrl()
                ))
                .sorted(Comparator.comparingDouble(VectorSearchMatch::score).reversed())
                .limit(safeTopK)
                .toList();
        return Mono.just(out);

    }

    private static double pseudoScore(String content, float[] embedding) {
        if (embedding == null || embedding.length == 0) return 0.0;
        int hash = Math.abs(content.hashCode());
        int bucket = hash % 100;
        return bucket / 100.0;
    }
}
