package fr.smartsoft.sz.ai.assistant.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(IllegalArgumentException ex) {
        String requestId = UUID.randomUUID().toString();
        return Map.of(
                "error", Map.of(
                        "code", "INVALID_REQUEST",
                        "message", ex.getMessage(),
                        "requestId", requestId
                )
        );
    }
}