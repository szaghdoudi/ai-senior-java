package fr.smartsoft.sz.ai.assistant.security;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.regex.Pattern;

@Component
public class PromptSafetyService {
    public record SafetyResult(boolean blocked, String rule) {
    }

    private record Rule(String id, Pattern pattern) {
    }

    private static final List<Rule> RULES = List.of(
            new Rule("INJECTION_IGNORE_INSTRUCTIONS",
                    Pattern.compile("(?i)ignore\\s+(all|previous|prior)\\s+instructions")),
            new Rule("INJECTION_OVERRIDE_SYSTEM",
                    Pattern.compile("(?i)(override|bypass|disable)\\s+(system|safety|policy)")),
            new Rule("INJECTION_ROLE_HIJACK",
                    Pattern.compile("(?i)you\\s+are\\s+now\\s+(developer|system|root|admin)")),
            new Rule("DATA_EXFIL_PROMPT",
                    Pattern.compile("(?i)(reveal|show|dump|print)\\s+.*(secret|token|password|api\\s*key|credentials?)"))
    );

    public SafetyResult evaluate(String question) {
        if (StringUtils.hasText(question)) {
            for (Rule rule : RULES) {
                if (rule.pattern.matcher(question).find()) {
                    return new SafetyResult(true, rule.id);
                }
            }
        }
        return new SafetyResult(false, "NONE");
    }

    public void ensureSafe(String question) {
        SafetyResult safetyResult = evaluate(question);
        if (safetyResult.blocked()) {
            throw new SecurityBlockedException("request blocked by prompt safety policy", safetyResult.rule());
        }
    }
}


