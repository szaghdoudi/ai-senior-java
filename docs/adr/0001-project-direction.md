# ADR-0002: Technology positioning — Java/Spring first with AI integration

## Status
ACCEPTED

## Context

The objective is to integrate AI capabilities into enterprise backend systems.

Most AI tutorials and ecosystems are Python-first and focus on:
- data science
- machine learning
- experimentation
- notebooks

However, in enterprise environments (bank/insurance), most production systems are based on:
- Java
- Spring Boot
- microservices
- Kubernetes
- secured environments

As a senior Java engineer, the goal is not to become a data scientist
but to become highly capable at integrating AI into existing systems.

## Decision

The project will follow a **Java/Spring-first approach**.

AI capabilities will be integrated into a Spring Boot backend,
using external LLM APIs and retrieval techniques.

Python may be explored later only if necessary,
but Java remains the primary implementation language.

The focus will be on:
- backend integration
- production constraints
- observability
- architecture
- reliability

## Consequences

This positioning allows:
- leveraging existing senior Java experience
- staying aligned with enterprise systems
- building rare and valuable skills
- avoiding unnecessary dispersion into full ML engineering

The project becomes a bridge between:
traditional backend architecture and modern AI capabilities.