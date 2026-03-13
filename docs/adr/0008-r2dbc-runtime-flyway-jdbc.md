# ADR-0008: Use R2DBC for runtime access and Flyway JDBC for schema migrations

## Status
ACCEPTED

## Date
2026-03-13

## Context
The application uses Spring WebFlux and a reactive runtime model.
RAG retrieval against PostgreSQL/pgvector should therefore avoid blocking database access in the request path.

At the same time, schema migrations must be reliable and standard.
Flyway remains JDBC-based and widely used for schema lifecycle management.


## Decision
Use:
- R2DBC for runtime database access in the application
- Flyway with JDBC for schema migration at startup

The PostgreSQL drive setup therefore contains both:
- reactive PostgreSQL access for runtime queries
- JDBC PostgreSQL access for Flyway migrations

## Consequences
Pros:
- Keeps runtime access aligned with WebFlux/reactive architecture
- Uses a standard and robust migration tool for schema management
- Clean separation between runtime query path and schema lifecycle concerns

Cons:
- Required two database connectivity configurations
- Slightly more setup complexity in application configuration
- Developers must understand that runtime DB access and migration DB access use different stacks

## Consequences
List expected impacts and tradeoffs.

Pros:
- ...

Cons:
- ...
