package fr.smartsoft.sz.ai.assistant.service.impl;


import fr.smartsoft.sz.ai.assistant.audit.AuditLogger;
import fr.smartsoft.sz.ai.assistant.config.RequestContextHolder;
import fr.smartsoft.sz.ai.assistant.dto.AiAskRequest;
import fr.smartsoft.sz.ai.assistant.dto.AiAskResponse;
import fr.smartsoft.sz.ai.assistant.llm.LlmClient;
import fr.smartsoft.sz.ai.assistant.llm.dto.ResolvedOptions;
import fr.smartsoft.sz.ai.assistant.rag.PromptBuilder;
import fr.smartsoft.sz.ai.assistant.rag.RetrievalQuery;
import fr.smartsoft.sz.ai.assistant.rag.RetrievalService;
import fr.smartsoft.sz.ai.assistant.rag.RetrievedChunk;
import fr.smartsoft.sz.ai.assistant.security.PromptSafetyService;
import fr.smartsoft.sz.ai.assistant.security.Redactor;
import fr.smartsoft.sz.ai.assistant.security.SecurityBlockedException;
import fr.smartsoft.sz.ai.assistant.service.AiService;
import fr.smartsoft.sz.ai.assistant.service.OptionsResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AiServiceImpl implements AiService {


    private final LlmClient llmClient;
    private final Redactor redactor;
    private final OptionsResolver optionsResolver;
    private final AuditLogger auditLogger;
    private final PromptSafetyService promptSafetyService;
    private final RetrievalService retrievalService;
    private final PromptBuilder promptBuilder;

    public AiServiceImpl(@Qualifier("OllamaLlmClient") LlmClient llmClient, Redactor redactor, OptionsResolver optionsResolver, AuditLogger auditLogger, PromptSafetyService promptSafetyService, RetrievalService retrievalService, PromptBuilder promptBuilder) {
        this.llmClient = llmClient;
        this.redactor = redactor;
        this.optionsResolver = optionsResolver;
        this.auditLogger = auditLogger;
        this.promptSafetyService = promptSafetyService;
        this.retrievalService = retrievalService;
        this.promptBuilder = promptBuilder;
    }

    public Mono<AiAskResponse> ask(AiAskRequest req) {

        return RequestContextHolder.getRequestId()
                .flatMap(requestId -> {
                    long t0 = System.currentTimeMillis();


                    String question = req == null ? null : req.question();
                    if (question == null || question.isBlank()) {
                        return Mono.error(new IllegalArgumentException("question must not be empty"));
                    }

                    String safeQuestion = redactor.redact(question);


                    int qLen = safeQuestion.length();

                    ResolvedOptions opts = optionsResolver.resolve(req);


                    try {
                        promptSafetyService.ensureSafe(question);
                    } catch (SecurityBlockedException e) {
                        auditLogger.info("ai.ask.start", Map.of(
                                "requestId", requestId,
                                "qLen", safeQuestion.length(),
                                "ragRequested", opts.ragUsed(),
                                "topKRequested", opts.topK()
                        ));
                        return Mono.error(e);
                    }


                    boolean ragUsed = req.options() != null && Boolean.TRUE.equals(req.options().useRag());
                    int topK = req.options() != null && req.options().topK() != null ? req.options().topK() : 5;
                    // Audit START (pas de contenu)
                    auditLogger.info("ai.ask.start", Map.of(
                            "requestId", requestId,
                            "qLen", safeQuestion.length(),
                            "ragRequested", ragUsed,
                            "topKRequested", topK
                    ));

                    Mono<List<RetrievedChunk>> retrievedMono = opts.ragUsed()
                            ? retrievalService.retrieve(new RetrievalQuery(safeQuestion, opts.topK()))
                            : Mono.just(List.of());
                    return retrievedMono.flatMap(chunks -> {
                        String prompt = promptBuilder.build(safeQuestion, chunks);

                        long llmStart = System.currentTimeMillis();
                        return llmClient.ask(prompt)
                                .map(llm -> {
                                    long llmMs = System.currentTimeMillis() - llmStart;
                                    long total = System.currentTimeMillis() - t0;
                                    // Audit END (toujours sans contenu)
                                    auditLogger.info("ai.ask.end", Map.of(
                                            "requestId", requestId,
                                            "provider", llm.provider(),
                                            "model", llm.model(),
                                            "llmMs", llmMs,
                                            "totalMs", total
                                    ));

                                    List<AiAskResponse.Citation> citations = chunks.stream()
                                            .map(c -> new AiAskResponse.Citation(
                                                    c.docId(),
                                                    c.docTitle(),
                                                    c.chunkId(),
                                                    c.score(),
                                                    AiAskResponse.SourceType.valueOf(c.sourceType().name()),
                                                    c.sourceUrl()
                                            ))
                                            .collect(Collectors.toList());

                                    return new AiAskResponse(
                                            llm.content(),
                                            citations,
                                            new AiAskResponse.Meta(
                                                    requestId,
                                                    llm.model(),
                                                    llm.provider(),
                                                    ragUsed,
                                                    new AiAskResponse.Meta.Retrieval(topK),
                                                    Map.of("retrieval", 0L, "llm", llmMs, "total", total),
                                                    new AiAskResponse.Meta.Cost(0, 0, 0, 0.0)
                                            )
                                    );
                                });
                    });
                });
    }


}