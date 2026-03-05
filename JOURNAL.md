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
