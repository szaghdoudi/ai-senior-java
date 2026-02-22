# ADR-0005: API responses include citations and request metadata

## Status
ACCEPTED

## Context
Enterprise AI must be auditable and explainable.
Users and auditors need to know what sources influenced an answer.

## Decision
Every answer response includes:
- `requestId` (traceability)
- `citations` (doc/chunk references + relevance score)
- `model/provider`
- timings and cost fields (even if initially 0)

## Consequences
- Stronger compliance posture
- Easier debugging and monitoring
- Slightly more implementation work, but worth it