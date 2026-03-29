# Project Checkpoint - 2026-03-13

## Summary
The RAG infrastructure is now connected to PostgreSQL/pgvector and Flyway migrations are operational.

## What is working
- PostgreSQL container is available for local development.
- Flyway runs successfully at application startup.
- `rag_chunks` schema is created in PostgreSQL.
- Runtime retrieval path is connected through R2DBC and `PgVectorStore`.
- Application starts successfully with the `rag-pgvector` profile.

## What is not done yet
- No ingestion pipeline exists yet.
- `rag_chunks` is empty.
- Effective document retrieval is therefore not available in practice.

## Architectural status
- Runtime DB access uses R2DBC.
- Schema migrations use Flyway with JDBC.
- Retrieval contracts and vector-store abstraction remain in place.

## Next priority
Implement the ingestion pipeline to populate `rag_chunks` from local documentation.
