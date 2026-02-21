# System Design — AI Dev Assistant

## Problem
Developers need fast, reliable answers about internal systems (APIs, specs, runbooks) without searching across many sources.

## Users
- Backend developers (primary)
- SRE/Support L2 (secondary)

## High-level architecture
- Client (CLI/HTTP/optional UI)
- AI API (Spring Boot): main entrypoint
- Retrieval layer: fetch relevant chunks from internal docs (vector store)
- LLM provider: OpenAI-compatible (pluggable)
- Observability: logs/metrics/traces
- Governance: document sources, access control, redaction

## Data flow (Ask)
1. User sends question to AI API
2. Retrieval searches relevant documents (top-k chunks)
3. AI API builds prompt with:
    - system policy
    - retrieved context
    - user question
4. LLM generates answer
5. AI API returns answer + metadata:
    - doc ids used
    - model/version
    - timing + cost estimate (later)

## Non-functional requirements (bank/insurance)
- No secrets in prompts
- PII redaction before sending to provider
- Audit trail for every answer
- Cost control (limits + caching)
- Fallback behavior if LLM is down