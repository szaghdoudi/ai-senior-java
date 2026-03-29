# Roadmap

## Status snapshot (2026-03-14)
- Phase 1: completed
- Phase 2: in progress
- Phase 3: planned
- Phase 4: planned

## Phase 1 — Foundations (completed)
- Project skeleton + doc structure
- Initial API contract and architecture baseline
- Provider strategy defined (pluggable via `LlmClient`)

## Phase 2 — RAG MVP (in progress)
- Prompt injection protection
- Document ingestion (implemented for local markdown docs)
- Embeddings + vector store (pgvector integrated, current embeddings are placeholder-based)
- Ask endpoint with retrieval + citations (operational end-to-end, relevance tuning pending real embeddings)

## Phase 3 — Production hardening (planned)
- Audit logs (request traceability + model/provider metadata)
- Cost guardrails (token budgets, caching)
- Security hardening (PII redaction tuning, allowlist, secrets management)
- Monitoring + runbooks + tracing

## Phase 4 — Career packaging (planned)
- Interview story + architecture diagrams
- CV bullet points + demo script
