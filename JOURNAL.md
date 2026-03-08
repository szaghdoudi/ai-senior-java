# Journal

## 2026-02-21
- Started "AI Dev Assistant" project as a career/enterprise-oriented track.
- Decided to use doc-as-code to avoid losing context across chats.
- Next:
  - Write initial system design doc (components + data flow)
  - Add first ADRs (provider choice, architecture style)

## 2026-02-22
- Added runbooks RB-001/RB-002.
- Added threat model (light).
- Added ADR-0006 (redaction before LLM).
- Next: decide initial MVP scope and start coding `/api/ai/ask` following the API contract (with `requestId` + placeholders for `citations/meta`).

## 2026-02-22
- Implemented `/api/ai/ask` API contract (MVP mock).
- Added `requestId` + meta fields + error handler.
- Switched LLM integration to full reactive (no `block()`).
- Added `LlmResult(provider/model/content)` and real LLM timings.
- Updated system prompt with glossary ADR.
- Next: add structured audit logging per `requestId` (without storing sensitive content).

## 2026-03-05
- Confirmed the project direction: enterprise-grade AI backend integration (bank/insurance style), not a toy chatbot.
- API contract implemented: `POST /api/ai/ask` returning `answer + citations[] + meta (requestId, provider, model, timings)`.
- Integrated local LLM provider using Ollama via `WebClient` (non-blocking).
- Added system prompt rules + glossary to avoid ambiguous terms (`ADR = Architecture Decision Record`).
- Implemented security MVP:
  - redaction before calling LLM (keys/tokens/emails/IBAN patterns)
- Implemented audit MVP:
  - `AUDIT` logger with `ai.ask.start / ai.ask.end`
  - logs contain metadata only (no prompt content)
- Verified runtime logs:
  - `provider=ollama`, `model=llama3.2`
  - LLM latency ~23–26s on current machine
- Decisions/notes:
  - OpenTelemetry/tracing postponed.
- Next:
  - stabilize request correlation approach (MDC vs structured audit fields vs Reactor context)
  - implement prompt injection guard (policy enforcement / prompt firewall)
  - start RAG MVP (vector store + citations) aligned with ADRs

## 2026-03-07
- Added PromptSafetyService (prompt injection guard before LLM call)
- Added SecurityBlockedException and mapped to API error code `SECURITY_BLOCKED`.
- Update error handling to return consistent error payload with `requestId`.

Why
- Block prompt-injection patterns early to reduce security risk before provider call.

Impact
- Security: suspicious prompts can be blocked before `LlmClient.ask`.
- API: blocked requests return `SECURITY_BLOCKED`.
- Audit: blocked events are traceable via `requestId`.

Next
- Start RAG MVP scaffold (`RetrievalSErvice` contract + citations flow)

## 2026-03-08

- Started RAG MVP implementation with a clean contract-first approach
  - added `RetrievalQuery`, `RetrievedChunk`, `SourceType`, `RetrievalService`
  - added `InMemoryRetrievalService` as first adapter
- Integrated retrieval flow into `AiServiceImpl`
  - when use `useRag=true`, retrieve topK chunks and inject context into prompt
  - map retrieved chunks to API `citations[]`
- Fixed security flow correctness:
  - prompt-safety block now return error (`Mono.error`) instead of only logging
- Added/updated tests:
  - retrieval unit test for ranking/topK behavior
  - Controller RAG integration test validates `meta.ragUsed`, `topK` and non-empty citations
- Stabilized tests for CI/sabdbox
  - switched controller integration test to `WebEnvironment.MOCK`
  - externalized reusable test config `StubLlmConfig`
  - stubbed LLM bean with qualifier-compatible name (`OllamaLllmCLient`) to avoid real calls

Why
- Validate end-to-end RAG orchestration before introducing pgvector complexity 
- Keep tests deterministic, fast and independent of external LLM/network

Next
- Introduce `VectorStore` port and `EmbeddingProvider` port.
- Keep `RetrievalService` orchestration stable with replacing in-memory adapter with pgvector adapter
