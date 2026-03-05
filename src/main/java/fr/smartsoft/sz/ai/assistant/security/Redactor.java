package fr.smartsoft.sz.ai.assistant.security;

import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class Redactor {

    // MVP: patterns à enrichir au fil du temps (via ADR)
    private static final Pattern OPENAI_KEY = Pattern.compile("sk-[A-Za-z0-9]{20,}");
    private static final Pattern BEARER = Pattern.compile("Bearer\\s+[A-Za-z0-9\\-\\._~\\+\\/]+=*", Pattern.CASE_INSENSITIVE);
    private static final Pattern EMAIL = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}");
    private static final Pattern IBAN = Pattern.compile("\\b[A-Z]{2}[0-9]{2}[A-Z0-9]{11,30}\\b"); // rough

    public String redact(String input) {
        if (input == null) return null;
        String out = input;
        out = OPENAI_KEY.matcher(out).replaceAll("sk-***REDACTED***");
        out = BEARER.matcher(out).replaceAll("Bearer ***REDACTED***");
        out = EMAIL.matcher(out).replaceAll("***REDACTED_EMAIL***");
        out = IBAN.matcher(out).replaceAll("***REDACTED_IBAN***");
        return out;
    }
}