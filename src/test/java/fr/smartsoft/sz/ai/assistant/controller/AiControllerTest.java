package fr.smartsoft.sz.ai.assistant.controller;

import fr.smartsoft.sz.ai.assistant.StubLlmConfig;
import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Import(StubLlmConfig.class)
class AiControllerTest {

    @Autowired
    ApplicationContext applicationContext;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
                .build();
    }

    @Test
    void shouldReturnCitationsWhenRagEnabled() {
        AiAskRequest req = new AiAskRequest(
                "How to handle provider timeout and retries in /api/ai/ask?",
                null,
                new AiAskRequest.Options(true, 2, 0.2, 400)
        );

        webTestClient.post()
                .uri("/api/ai/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(req)
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectHeader().exists("X-Request-Id")
                .expectBody()
                .jsonPath("$.meta.ragUsed").isEqualTo(true)
                .jsonPath("$.meta.retrieval.topK").isEqualTo(2)
                .jsonPath("$.citations").isArray()
                .jsonPath("$.citations.length()").value(v -> {
                    int size = Integer.parseInt(v.toString());
                    if (size < 1) throw new AssertionError("expected at least 1 citation");
                })
                .jsonPath("$.citations[0].docId").exists()
                .jsonPath("$.citations[0].chunkId").exists()
                .jsonPath("$.citations[0].score").exists();
    }
}
