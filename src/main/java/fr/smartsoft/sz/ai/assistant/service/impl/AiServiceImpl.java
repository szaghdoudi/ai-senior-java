package fr.smartsoft.sz.ai.assistant.service.impl;


import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import fr.smartsoft.sz.ai.assistant.dto.AiAskResponse;
import fr.smartsoft.sz.ai.assistant.llm.LlmClient;
import fr.smartsoft.sz.ai.assistant.service.AiService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AiServiceImpl implements AiService {


    private final LlmClient llmClient;

    public AiServiceImpl(@Qualifier("OllamaLlmClient")LlmClient llmClient) {
        this.llmClient = llmClient;
    }

    public Mono<AiAskResponse> ask(AiAskRequest req) {
        long t0 = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        String question = req == null ? null : req.question();
        if (question == null || question.isBlank()) {
            return Mono.error(new IllegalArgumentException("question must not be empty"));
        }

        boolean ragUsed = req.options() != null && Boolean.TRUE.equals(req.options().useRag());
        int topK = req.options() != null && req.options().topK() != null ? req.options().topK() : 5;

        String prompt = question;

        long llmStart = System.currentTimeMillis();
        return llmClient.ask(prompt)
                .map(llm -> {
                    long llmMs = System.currentTimeMillis() - llmStart;
                    long total = System.currentTimeMillis() - t0;

                    return new AiAskResponse(
                            llm.content(),
                            List.of(), // no RAG yet
                            new AiAskResponse.Meta(
                                    requestId,
                                    llm.model(),
                                    llm.provider(),
                                    ragUsed,
                                    new AiAskResponse.Meta.Retrieval(topK),
                                    Map.of("retrieval", 0L, "llm", llmMs, "total", total),
                                    new AiAskResponse.Meta.Cost(0, 0, 0, 0.0)
                            )
                    );
                });
    }
}