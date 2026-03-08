package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.RetrievalQuery;
import fr.smartsoft.sz.ai.assistant.rag.RetrievalService;
import fr.smartsoft.sz.ai.assistant.rag.RetrievedChunk;
import fr.smartsoft.sz.ai.assistant.rag.SourceType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InMemoryRetrievalService implements RetrievalService {


    private static final List<RetrievedChunk> KB = List.of(
            new RetrievedChunk(
                    "rb-001",
                    "LLM provider down / timeouts",
                    "rb-001-c1",
                    "If latency increases, verify provider status, timeout settings, and retries.",
                    0.0,
                    SourceType.MARKDOWN,
                    "docs/runbooks/RB-001-llm-provider-down.md"
            ),
            new RetrievedChunk(
                    "arch-runtime",
                    "Runtime defaults and guardrails",
                    "arch-runtime-c1",
                    "Default topK is 5, maxTokens is 800, and server-side guardrails clamp values.",
                    0.0,
                    SourceType.MARKDOWN,
                    "docs/architecture/runtime-defaults.md"
            ),
            new RetrievedChunk(
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
    public Mono<List<RetrievedChunk>> retrieve(RetrievalQuery retrievalQuery) {
        if (retrievalQuery == null || !StringUtils.hasText(retrievalQuery.query())) {
            return Mono.just(List.of());
        }
        int topK = Math.max(1, retrievalQuery.topK());
        Set<String> terms = tokenize(retrievalQuery.query());
        List<RetrievedChunk> rancked = KB.stream()
                .map(chunk -> withScore(chunk, score(chunk.content(), terms)))
                .filter(chunk -> chunk.score() > 0)
                .sorted(Comparator.comparingDouble(RetrievedChunk::score).reversed())
                .limit(topK)
                .toList();
        return Mono.just(rancked);
    }

    private static Set<String> tokenize(String text) {
        return List.of(text.toLowerCase(Locale.ROOT).split("[^a-z0-9]+"))
                .stream()
                .filter(s -> !s.isBlank())
                .collect(Collectors.toSet());
    }

    private static RetrievedChunk withScore(RetrievedChunk chunk, double score) {
        return new RetrievedChunk(
                chunk.docId(),
                chunk.docTitle(),
                chunk.chunkId(),
                chunk.content(),
                score,
                chunk.sourceType(),
                chunk.sourceUrl()
        );
    }

    private static double score(String content, Set<String> terms) {
        String lc = content.toLowerCase(Locale.ROOT);
        long hits = terms.stream().filter(lc::contains).count();
        if (terms.isEmpty()) return 0.0;
        return (double) hits / terms.size();
    }

}
