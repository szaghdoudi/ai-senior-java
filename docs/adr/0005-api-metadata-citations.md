# ADR-0005: API responses include citations and request metadata

## Status
ACCEPTED

## Date
2026-03-05 (normalized; original decision predates this edit)

## Context
Enterprise AI must be auditable and explainable. Users and auditors need to know which sources influenced an answer.

## Decision
Every answer response includes:
- `requestId` (traceability)
- `citations` (doc/chunk references + relevance score)
- `model` and `provider`
- timing and cost fields (even if initially zero)

## Consequences
- Stronger compliance posture
- Easier debugging and monitoring
- Slightly more implementation work, but worth it
