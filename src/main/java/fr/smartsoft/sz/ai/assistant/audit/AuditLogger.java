package fr.smartsoft.sz.ai.assistant.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component

public class AuditLogger {
    private static final Logger audit = LoggerFactory.getLogger("AUDIT");

    public void info(String event, Map<String, Object> fields) {
        // Simple JSON-like line (suffisant MVP)
        audit.info("{} {}", event, fields);
    }
}
