package fr.smartsoft.sz.ai.assistant.controller;


import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import fr.smartsoft.sz.ai.assistant.dto.AiAskResponse;
import fr.smartsoft.sz.ai.assistant.service.impl.AiServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiServiceImpl aiServiceImpl;

    public AiController(AiServiceImpl aiServiceImpl) {
        this.aiServiceImpl = aiServiceImpl;
    }

    @PostMapping("/ask")
    public Mono<AiAskResponse> ask(@RequestBody AiAskRequest req) {
        return aiServiceImpl.ask(req);
    }
}