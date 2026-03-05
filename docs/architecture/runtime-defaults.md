# Runtime defaults & guardrails

## Defaults (if request.options is missing)
- useRag: false
- topK: 5
- temperature: 0.2
- maxTokens: 800

## Guardrails (server-side)
- question must be non-empty
- topK:
    - min: 1
    - max: 10
    - if useRag=false, topK is accepted but has no effect (kept for consistency)
- temperature:
    - min: 0.0
    - max: 1.0
- maxTokens:
    - min: 64
    - max: 2000

## Behavior
- ragUsed in response reflects the effective value after validation/guardrails
- retrieval.topK in response reflects the effective value after validation/guardrails