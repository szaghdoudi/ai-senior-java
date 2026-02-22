# RB-002 — Quota exceeded / cost anomaly

## Symptoms
- Provider returns 429 (rate limit) or quota exceeded
- Sudden increase in `estimatedUsd` / token usage
- Higher average tokens per request

## Impact
- Requests fail or get throttled
- Costs can grow unexpectedly

## Immediate checks
1. Check traffic volume (requests/min)
2. Check token usage distribution
    - top users / endpoints / question sizes
3. Identify recent changes:
    - prompt template expanded?
    - RAG topK increased?
    - maxTokens increased?
4. Verify caching effectiveness (hit ratio)

## Mitigation
- Enforce stricter guardrails:
    - lower `maxTokens`
    - cap question size
    - reduce `topK`
- Enable/strengthen caching
- Apply rate limiting per user/client (if available)
- Block abusive patterns (very long prompts, repeated requests)

## Recovery
- Request quota increase only after guardrails are in place
- Monitor cost and token metrics daily for 1 week

## Post-incident actions
- Add budget alerts (daily/weekly)
- Add dashboards: cost per day, tokens per request
- Update ADR if provider strategy changes