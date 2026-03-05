# Project Checkpoint — 2026-03-05

## Goal

Build an enterprise-grade AI backend service in Java/Spring capable of integrating LLMs while respecting enterprise constraints:

- security
- auditability
- reliability
- future observability

The focus is **LLM integration in production systems**, not model training.

---

## Current Architecture

Client
↓
POST /api/ai/ask
↓
Controller (Spring WebFlux)
↓
AiService
↓
LlmClient abstraction
↓
Ollama (local LLM)

Key design rule:

> LLM provider must be replaceable.

Future providers may include:

- OpenAI
- Azure OpenAI
- Mistral
- internal LLM

---

## Implemented Features

### API

Endpoint:

POST /api/ai/ask

Response includes:

- answer
- citations[]
- meta
    - requestId
    - provider
    - model
    - timings

---

### LLM Integration

Implemented provider:

Ollama via WebClient (non-blocking).

Abstraction layer:

LlmClient interface

This allows future provider replacement.

---

### Security (MVP)

Implemented protections:

Redaction before sending prompt to LLM.

Sensitive data patterns detected:

- API keys
- Bearer tokens
- emails
- IBAN

Example:

Bearer abc123 → Bearer ***REDACTED***

---

### Prompt Policy

System prompt includes:

- enterprise backend context
- glossary rules
- ambiguity handling

Example:

ADR = Architecture Decision Record

This prevents incorrect interpretations.

---

### Audit Logging

Audit events implemented:

ai.ask.start  
ai.ask.end  
ai.ask.error

Important rule:

Prompt content is never logged.

Only metadata is stored.

---

### Request Correlation

Each request produces a:

requestId

Used in:

- API response meta
- audit logs
- troubleshooting

---

## Current Performance

Typical latency (local machine):

≈ 23–26 seconds

Provider: ollama  
Model: llama3.2

Most latency comes from the LLM.

---

## Documentation Structure

docs/

adr/ → architectural decisions  
api/ → API contract  
architecture/ → system design + threat model  
notes/ → checkpoints and glossary  
runbooks/ → operational procedures

---

## Next Steps

Priority order:

1. Prompt injection protection
2. RAG retrieval (vector search + citations)
3. Observability (OpenTelemetry)