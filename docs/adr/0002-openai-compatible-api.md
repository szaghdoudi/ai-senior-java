# ADR-0002: Start with an OpenAI-compatible API (pluggable provider)

## Status
Proposed

## Context
We want a fast start while keeping the ability to swap providers (Azure OpenAI, Mistral, on-prem).

## Decision
Use an OpenAI-compatible HTTP interface initially, behind a Java interface (LlmClient) to keep it replaceable.

## Consequences
- Quick initial progress
- Provider can be swapped later with minimal impact