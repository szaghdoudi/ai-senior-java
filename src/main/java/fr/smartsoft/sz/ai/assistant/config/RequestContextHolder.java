package fr.smartsoft.sz.ai.assistant.config;

import reactor.core.publisher.Mono;

public final class RequestContextHolder {

    public static final String REQUEST_ID = "requestId";

    private RequestContextHolder() {}

    public static Mono<String> getRequestId() {
        return Mono.deferContextual(ctx ->
                Mono.justOrEmpty(ctx.getOrEmpty(REQUEST_ID)));
    }
}