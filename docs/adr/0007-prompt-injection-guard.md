# ADR-0007: Add PromptSafetyService before LLM provider call

## Status
ACCEPTED

## Date
2026-03-07

## Context
Prompt-injection attempts can bypass system intent and trigger unsafe behavior or data disclosure.

## Decision
Introduce a PromptSafetyService in the request flow before redaction/provider call.
If a rule is matched, block request with `SECURITY_BLOCKED` and return a traceable error with `requestId`

## Consequences
Pros:
- Earlier security barrier
- Clear API semantics for blocked requests
- Better auditability for security events

Cons:
- Rule tuning overhead
- Risk of false positives/false negatives