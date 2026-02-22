package fr.smartsoft.sz.ai.assistant.llm;

import fr.smartsoft.sz.ai.assistant.llm.dto.LlmResult;
import reactor.core.publisher.Mono;

public interface LlmClient {
    Mono<LlmResult> ask(String prompt);
}