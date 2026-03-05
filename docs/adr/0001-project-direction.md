# ADR-0001: Technology positioning — Java/Spring first with AI integration

## Status
ACCEPTED

## Date
2026-03-05 (normalized; original decision predates this edit)

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

As a senior Java engineer, the goal is not to become a data scientist but to become highly capable at integrating AI into existing systems.

## Decision
The project follows a Java/Spring-first approach.

AI capabilities are integrated into a Spring Boot backend using external LLM APIs and retrieval techniques.

Python may be explored later only if necessary, but Java remains the primary implementation language.

Focus areas:
- backend integration
- production constraints
- observability
- architecture
- reliability

## Consequences
- Leverages existing senior Java experience
- Stays aligned with enterprise systems
- Builds rare and valuable skills
- Avoids dispersion into full ML engineering

The project becomes a bridge between traditional backend architecture and modern AI capabilities.
