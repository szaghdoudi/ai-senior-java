# ADR-0002: Start with a pluggable provider abstraction (OpenAI-compatible first)

## Status
ACCEPTED

## Date
2026-03-05 (normalized; original decision predates this edit)

## Context
Fast implementation progress is needed while preserving provider portability (Azure OpenAI, OpenAI-compatible APIs, local/on-prem models such as Ollama/Mistral).

## Decision
Use a provider abstraction (`LlmClient`) as the stable integration boundary.

Initial strategy:
- prefer OpenAI-compatible API shape for portability
- allow non-OpenAI adapters behind the same interface (current runtime: Ollama adapter)

## Consequences
- Fast progress with low coupling to one provider
- Provider can be swapped with limited impact on API/controller layers
- Slight additional complexity due to adapter/interface maintenance
