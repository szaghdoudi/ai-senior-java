package fr.smartsoft.sz.ai.assistant.rag.impl;

import fr.smartsoft.sz.ai.assistant.rag.EmbeddingProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@Primary
public class OllamaEmbeddingProvider implements EmbeddingProvider {

    private final WebClient webClient;
    private final String model;

    public OllamaEmbeddingProvider(WebClient.Builder builder,
                                   @Value("${llm.ollama.base-url}") String baseUrl,
                                   @Value("${llm.ollama.embedding-model}") String model) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.model = model;
    }

    @Override
    public Mono<float[]> embed(String text) {
        if (!StringUtils.hasText(text)) {
            return Mono.just(new float[0]);
        }

        Map<String, Object> body = Map.of(
                "model", model,
                "input", text
        );

        return webClient.post().uri("/api/embed")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(30))
                .map(this::extractEmbedding);
    }

    private float[] extractEmbedding(Map<String, Object> response) {
        Object rawEmbeddings = response.get("embeddings");
        if (!(rawEmbeddings instanceof List<?> embeddings) || embeddings.isEmpty()) {
            throw new IllegalArgumentException("Ollama embedding response does not contain embeddings");
        }
        Object first = embeddings.getFirst();
        if (!(first instanceof List<?> values) || values.isEmpty()) {
            throw new IllegalArgumentException("Ollama embedding response contains an empty embedding");
        }
        float[] result = new float[values.size()];
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if (!(value instanceof Number number)) {
                throw new IllegalArgumentException("Invalid embedding value at index " + i);
            }
            result[i] = number.floatValue();
        }
        return result;
    }
}
