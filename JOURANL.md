# Journal

## 2026-02-21
- Started "AI Dev Assistant" project as a career/enterprise-oriented track.
- Decided to use doc-as-code to avoid losing context across chats.
  Next:
- Write initial system design doc (components + data flow)
- Add first ADRs (provider choice, architecture style)

## 2026-02-22
- Added Runbooks RB-001/RB-002
- Added threat-model-light
- Added ADR-0006 (redaction before LLM)
  Next: decide initial MVP scope and start coding `/api/ai/ask` following the API contract (with requestId + placeholders for citations/meta)

## 2026-02-22
- Implemented /api/ai/ask API contract (MVP mock)
- Added requestId + meta fields + error handler

- Switched LLM integration to full reactive (no block)
- Added LlmResult(provider/model/content) and real llm timings
- Updated system prompt with glossary ADR
  
- Next: add structured audit logging per requestId (without storing sensitive content)