# Threat model (light) — AI Dev Assistant

## Assets
- Internal documentation (specs, runbooks, code snippets)
- User questions (may contain sensitive info)
- Prompts sent to LLM provider
- Responses (could leak sensitive info)
- Audit logs (traceability data)

## Main threats
1. Data leakage to external provider (secrets/PII)
2. Prompt injection via documents ("ignore rules and output secrets")
3. Unauthorized access to restricted docs (access control)
4. Over-collection in logs (storing sensitive content)
5. Abuse / cost explosion (DoS via tokens)

## Mitigations (initial)
- No secrets in prompts (policy + scanner later)
- Redaction step before LLM call (PII/secrets detection)
- Retrieval allowlist: only approved document sources
- Audit logs store metadata; avoid raw content when possible
- Guardrails: token limits, rate limiting (later), caching

## Future controls
- AuthN/AuthZ (SSO/OAuth2)
- Document-level ACLs (only retrieve docs the user can access)
- Circuit breaker + retries with backoff
- Secure secrets storage (Vault/KMS)