# ADR-0006: Redact PII/secrets before calling any LLM provider

## Status
ACCEPTED

## Context
In bank/insurance environments, user inputs or internal docs may contain:
- credentials, tokens, private keys
- personal data (names, emails, IBAN, etc.)

Sending such data to an external provider can violate security/compliance.

## Decision
Introduce a redaction/safety layer in the AI API:
- scan user question and retrieved chunks
- mask detected secrets/PII (or block request)
- log the event without storing the sensitive content

## Consequences
- Reduced risk of data leakage
- Slight latency overhead
- Requires ongoing tuning (false positives/negatives)