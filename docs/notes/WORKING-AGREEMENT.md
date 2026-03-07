# Working Agreement (Learning Mode)

## Purpose
This document defines how we collaborate during this project so guidance stays consistent across sessions.

## Learner Profile
- You are a senior Java developer.
- Current objective: master enterprise-grade AI backend integration, with RAG as a primary goal.
- Current mode: learning-first (deep understanding before speed).

## Response Style Requirements
For non-trivial topics, answers must follow this structure:
1. Why (intent and problem framing)
2. Design (architecture and responsibilities)
3. Code (implementation proposal)
4. Tests (what to validate and why)
5. Tradeoffs (pros, cons, risks)
6. Memo (short retention summary)

## Engineering Quality Bar
- No quick hacks or throwaway shortcuts in core flows.
- Prefer clean architecture boundaries (ports/adapters, separation of concerns).
- Use recent, stable practices aligned with current Java/Spring ecosystem.
- Keep API contracts explicit and backward-compatible when possible.
- Security, auditability, and observability are first-class concerns.

## RAG Implementation Principles
- Build contract-first (domain model and interfaces before adapters).
- Start with a controlled adapter only to validate orchestration behavior.
- Replace adapters without changing orchestration contracts.
- Keep citations and traceability as mandatory outputs.

## Testing Principles
- Unit tests for business logic and safety rules.
- Integration tests for API behavior and error contracts.
- Assertions should focus on stable semantics (e.g., numeric HTTP code + payload contract).

## Documentation Update Rule
Every meaningful code increment must update documentation in the same cycle:
- `JOURNAL.md`: what changed + why + next step
- `ROADMAP.md`: phase/status update when scope moves
- `docs/api/api-contract.md`: if request/response or errors change
- `docs/adr/*.md`: only when a real architecture decision is made
- `docs/notes/checkpoint-YYYY-MM-DD.md`: periodic consolidated snapshot

## Current Priority Order
1. RAG MVP (primary objective)
2. Security hardening of the AI flow
3. Observability and production readiness
