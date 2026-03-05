# ADR-0004: Vector store strategy — start with pgvector, keep it replaceable

## Status
ACCEPTED (initial choice, revisitable)

## Date
2026-03-05 (normalized; original decision predates this edit)

## Context
RAG requires a vector store for similarity search (top-k chunks).

In enterprise environments, typical options are:
- PostgreSQL + pgvector (simple, cheap, good enough for MVP)
- Elasticsearch (often already available, strong operationally)
- Dedicated vector DB (rare in banks due to governance)

A fast start, minimal ops, and an easy migration path are required.

## Decision
Start with PostgreSQL + pgvector for the MVP.

Expose retrieval behind an interface (`VectorStore` / `RetrievalRepository`) so the implementation can switch later to Elasticsearch without changing the AI orchestration layer.

## Consequences
Pros:
- Simple to run locally and in CI
- Reuses a common enterprise component (Postgres)
- Good enough for early RAG use cases

Cons:
- Less advanced search features than Elasticsearch at scale
- Potential migration needed if usage grows significantly

Migration plan:
- Keep chunk IDs stable
- Keep embedding model/version in metadata
- Implement Elasticsearch adapter later if needed
