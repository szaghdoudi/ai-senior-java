package fr.smartsoft.sz.ai.assistant.service;

import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import fr.smartsoft.sz.ai.assistant.llm.dto.ResolvedOptions;
import org.springframework.stereotype.Component;

@Component
public class OptionsResolver {

    // Defaults
    private static final boolean DEFAULT_RAG = false;
    private static final int DEFAULT_TOPK = 5;
    private static final double DEFAULT_TEMP = 0.2;
    private static final int DEFAULT_MAX_TOKENS = 800;

    // Guardrails
    private static final int TOPK_MIN = 1;
    private static final int TOPK_MAX = 10;
    private static final double TEMP_MIN = 0.0;
    private static final double TEMP_MAX = 1.0;
    private static final int MAXTOK_MIN = 64;
    private static final int MAXTOK_MAX = 2000;

    public ResolvedOptions resolve(AiAskRequest req) {
        AiAskRequest.Options opt = (req == null) ? null : req.options();

        boolean ragUsed = opt != null && opt.useRag() != null ? opt.useRag() : DEFAULT_RAG;
        int topK = opt != null && opt.topK() != null ? opt.topK() : DEFAULT_TOPK;
        double temperature = opt != null && opt.temperature() != null ? opt.temperature() : DEFAULT_TEMP;
        int maxTokens = opt != null && opt.maxTokens() != null ? opt.maxTokens() : DEFAULT_MAX_TOKENS;

        // Guardrails
        topK = clamp(topK, TOPK_MIN, TOPK_MAX);
        temperature = clamp(temperature, TEMP_MIN, TEMP_MAX);
        maxTokens = clamp(maxTokens, MAXTOK_MIN, MAXTOK_MAX);

        return new ResolvedOptions(ragUsed, topK, temperature, maxTokens);
    }

    private static int clamp(int v, int min, int max) {
        return Math.max(min, Math.min(max, v));
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}