package fr.smartsoft.sz.ai.assistant.controller;


import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import fr.smartsoft.sz.ai.assistant.dto.AiAskResponse;
import fr.smartsoft.sz.ai.assistant.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AiController {

    private final AiService aiService;

    public AiController(AiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AiAskResponse> ask(@RequestBody AiAskRequest req) {
        return ResponseEntity.ok(aiService.ask(req));
    }
}