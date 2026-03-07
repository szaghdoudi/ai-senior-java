package fr.smartsoft.sz.ai.assistant.controller;

import fr.smartsoft.sz.ai.assistant.config.RequestIdWebFilter;
import fr.smartsoft.sz.ai.assistant.security.SecurityBlockedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;
import java.util.UUID;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> badRequest(IllegalArgumentException ex, ServerWebExchange exchange) {
        return error("INVALID_REQUEST", ex.getMessage(), resolveRequestId(exchange));
    }

    @ExceptionHandler(SecurityBlockedException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public Map<String, Object> securityBlocked(SecurityBlockedException ex, ServerWebExchange exchange) {
        return error("SECURITY_BLOCKED", ex.getMessage(), resolveRequestId(exchange));
    }

    private static Map<String, Object> error(String code, String message, String requestId) {
        return Map.of(
                "error", Map.of(
                        "code", code,
                        "message", message,
                        "requestId", requestId
                )
        );
    }

    private static String resolveRequestId(ServerWebExchange exchange) {
        String fromResponse = exchange.getResponse().getHeaders().getFirst(RequestIdWebFilter.HEADER);
        if (fromResponse != null && !fromResponse.isBlank()) {
            return fromResponse;
        }
        String fromRequest = exchange.getRequest().getHeaders().getFirst(RequestIdWebFilter.HEADER);
        if (fromRequest != null && !fromRequest.isBlank()) {
            return fromRequest;
        }
        return UUID.randomUUID().toString();
    }
}