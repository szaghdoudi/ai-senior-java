# RB-001 — LLM Provider down / timeouts

## Symptoms
- Increased latency on `/api/ai/ask`
- Errors: 5xx, timeout, PROVIDER_DOWN
- Spike in retry counts
- Provider returns 429/503

## Impact
- Users cannot get answers
- Potential cascading latency in upstream services

## Immediate checks
1. Confirm provider status (Azure/OpenAI status page if applicable)
2. Check recent deploys/config changes (baseUrl, model)
3. Inspect logs by `requestId`:
    - provider response code
    - timeout duration
    - retry attempts
4. Metrics to check:
    - `ai_llm_latency_ms`
    - `ai_llm_errors_total`
    - `ai_requests_total`

## Mitigation
- Enable fallback mode:
    - return a controlled message: "AI provider currently unavailable"
    - optionally return cached answers if available
- Reduce traffic:
    - temporarily lower rate limits / enable circuit breaker open
- Increase timeouts cautiously (avoid saturating threads)

## Recovery steps
- Verify provider is back (successful health check)
- Gradually close circuit breaker
- Monitor latency & error rates for 30 minutes

## Post-incident actions
- Add/adjust circuit breaker thresholds
- Add runbook links into alert description
- If recurring: consider secondary provider (ADR)