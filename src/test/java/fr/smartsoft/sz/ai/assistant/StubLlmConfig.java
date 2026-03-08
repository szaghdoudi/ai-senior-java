package fr.smartsoft.sz.ai.assistant;

import fr.smartsoft.sz.ai.assistant.llm.LlmClient;
import fr.smartsoft.sz.ai.assistant.llm.dto.LlmResult;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import reactor.core.publisher.Mono;

@TestConfiguration
public class StubLlmConfig {
    @Bean("OllamaLlmClient")
    @Primary
    LlmClient testLlmClient() {
        return prompt -> Mono.just(new LlmResult(
                "stub-provider",
                "stub-model",
                "Stub answer for test"
        ));
    }
}
