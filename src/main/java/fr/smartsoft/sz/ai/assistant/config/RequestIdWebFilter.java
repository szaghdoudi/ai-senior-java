package fr.smartsoft.sz.ai.assistant.config;

import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class RequestIdWebFilter implements WebFilter {

    public static final String HEADER = "X-Request-Id";
    public static final String MDC_KEY = "requestId";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String requestId = exchange.getRequest().getHeaders().getFirst(HEADER);
        if (requestId == null || requestId.isBlank()) requestId = UUID.randomUUID().toString();

        final String rid = requestId;
        exchange.getResponse().getHeaders().set(HEADER, rid);

        return chain.filter(exchange)
                .contextWrite(ctx -> ctx.put(RequestContextHolder.REQUEST_ID, rid))
                .doFinally(signalType -> MDC.remove(MDC_KEY));
                
    }
}