package fr.smartsoft.sz.ai.assistant.llm.impl;

import fr.smartsoft.sz.ai.assistant.llm.LlmClient;
import fr.smartsoft.sz.ai.assistant.llm.dto.LlmResult;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@Component
@Qualifier("OllamaLlmClient")
public class OllamaLlmClient implements LlmClient {

    private final WebClient webClient;
    private final String model;

    public OllamaLlmClient(
            WebClient.Builder builder,
            @Value("${llm.ollama.base-url}") String baseUrl,
            @Value("${llm.ollama.model}") String model
    ) {
        this.webClient = builder.baseUrl(baseUrl).build();
        this.model = model;
    }

    @Override
    public Mono<LlmResult> ask(String prompt) {
        Map<String, Object> body = Map.of(
                "model", model,
                "messages", List.of(
                        Map.of("role", "system", "content",
                                """
                                You are a senior Java/Spring backend assistant in an enterprise (bank/insurance).
                                Glossary: ADR = Architecture Decision Record (software architecture).
                                If a term is ambiguous, ask a clarification question or state the assumption.
                                """),
                        Map.of("role", "user", "content", prompt)
                ),
                "stream", false
        );

        return webClient.post()
                .uri("/api/chat")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .timeout(Duration.ofSeconds(60))
                .map(resp -> new LlmResult("ollama", model, extractContent(resp)));
    }

    private static String extractContent(Map<?, ?> resp) {
        Object message = resp.get("message");
        if (message instanceof Map<?, ?> msgMap) {
            Object content = msgMap.get("content");
            if (content != null) return content.toString();
        }
        return "LLM error: unable to parse response";
    }

}