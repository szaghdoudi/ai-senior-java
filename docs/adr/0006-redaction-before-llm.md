# ADR-0006: Redact PII/secrets before calling any LLM provider

## Status
ACCEPTED

## Date
2026-03-05 (normalized; original decision predates this edit)

## Context
In bank/insurance environments, user inputs or internal docs may contain:
- credentials, tokens, private keys
- personal data (names, emails, IBAN, etc.)

Sending such data to an external provider can violate security/compliance requirements.

## Decision
Introduce a redaction/safety layer in the AI API:
- scan user question and retrieved chunks
- mask detected secrets/PII (or block the request)
- log the event without storing sensitive content

## Consequences
- Reduced risk of data leakage
- Slight latency overhead
- Requires ongoing tuning (false positives/negatives)
