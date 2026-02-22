package fr.smartsoft.sz.ai.assistant.service;

import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import fr.smartsoft.sz.ai.assistant.dto.AiAskResponse;
import reactor.core.publisher.Mono;

public interface AiService {
    Mono<AiAskResponse> ask(AiAskRequest req);
}
