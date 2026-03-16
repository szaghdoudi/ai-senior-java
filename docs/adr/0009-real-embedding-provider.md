# ADR-0009: Replace placeholder embeddings with a real embedding provider

## Status
PROPOSED

## Date
2026-03-14

## Context
The current RAG pipeline is operational end-to-end
- documents are loaded and chunked
- chunks are indexed into PostgreSQL/pgvector
- retrieval return citations through the API

However, the current embedding provider is a placeholder implementation is used only to validate pipeline wiring.
It does not produce semantically meaningful vectors, which limits retrieval quality and makes ranking unreliable.

To move from structural validation to useful RAG behavior, the system now needs a real embedding provider.

## Decision
Replace the placeholder embedding provider with a real provider implementation behind existing `EmbeddingProvider` port.

The selected provider should:
- produce embeddings with a stable and documented dimension
- be compatible with the current pgvector schema
- be swappable without changing ingestion or retrieval orchestration
- support production-oriented concerns such as configuration, timeout handling, and future observability

## Consequences
Pros:
- Significant improvement in retrieval quality and semantic ranking
- Keeps architecture stable through the existing `EmbeddingProvider` abstraction
- Allows ingestion and retrieval to benefit from same embedding contract

Cons:
- Adds provider dependency and operational complexity
- Requires re-indexing existing documents after provider activation
- Introduces cost, latency, or infrastructure considerations depending on the chosen provider

## Follow-up
A concrete provider decision should be recorded once the actual implementation is selected:
- OpenAI-compatible embedding API
- Azure openAI embeddings
- Local embeddings service