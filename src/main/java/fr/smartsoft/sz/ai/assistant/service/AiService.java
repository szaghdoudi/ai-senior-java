package fr.smartsoft.sz.ai.assistant.service;


import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import fr.smartsoft.sz.ai.assistant.dto.AiAskResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AiService {

    public AiAskResponse ask(AiAskRequest req) {
        long t0 = System.currentTimeMillis();
        String requestId = UUID.randomUUID().toString();

        // Guardrails MVP (simple)
        String question = req == null ? null : req.question();
        if (question == null || question.isBlank()) {
            throw new IllegalArgumentException("question must not be empty");
        }

        boolean ragUsed = req.options() != null && Boolean.TRUE.equals(req.options().useRag());
        int topK = req.options() != null && req.options().topK() != null ? req.options().topK() : 5;

        // MVP: no LLM yet, return mock answer to validate API + logs
        String answer = "MVP OK. Received question: " + question;

        long tTotal = System.currentTimeMillis() - t0;

        return new AiAskResponse(
                answer,
                List.of(), // citations empty (no RAG yet)
                new AiAskResponse.Meta(
                        requestId,
                        "TBD",          // model
                        "TBD",          // provider
                        ragUsed,
                        new AiAskResponse.Meta.Retrieval(topK),
                        Map.of(
                                "retrieval", 0L,
                                "llm", 0L,
                                "total", tTotal
                        ),
                        new AiAskResponse.Meta.Cost(
                                0, 0, 0, 0.0
                        )
                )
        );
    }
}