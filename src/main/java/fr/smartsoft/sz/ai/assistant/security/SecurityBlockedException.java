package fr.smartsoft.sz.ai.assistant.security;

public class SecurityBlockedException extends RuntimeException{
    private final String rule;

    public SecurityBlockedException(String message, String rule) {
        super(message);
        this.rule = rule;
    }

    public String getRule() {
        return rule;
    }
}
