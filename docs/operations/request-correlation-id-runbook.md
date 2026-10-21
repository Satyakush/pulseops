# Request Correlation ID Runbook

## Contract

PulseOps returns an `X-Request-Id` response header. A caller-supplied ID is accepted only when it is 1–128 ASCII characters from this allowlist: letters, digits, dot, underscore, colon, and hyphen. Missing or invalid values should be replaced by a generated UUID. The same resolved value should be visible in request context and safe API error responses.

## Triage

1. Capture the response's `X-Request-Id` value.
2. Search server logs for that exact ID; treat it as a diagnostic key, not proof of identity.
3. Compare the response header and error envelope request ID when an error body is present.
4. If no matching logs exist, check that the request traversed the configured servlet filter and that the logging pattern includes the correlation field.
5. Reproduce with a known-safe ID such as `ops-check-42`, then repeat with a malformed ID to verify fallback behavior.

## Security and privacy

- Never put access tokens, passwords, email addresses, or other personal data in request IDs.
- Do not trust a client-provided request ID for authorization or rate limiting.
- Keep IDs bounded to avoid log amplification and header abuse.
- Do not return stack traces or raw exception details alongside the correlation ID.

## Release checks

- [ ] Missing ID results in a generated response ID.
- [ ] Valid ID is preserved exactly.
- [ ] Empty, whitespace, control-character, unsupported-character, and overlong IDs fall back safely.
- [ ] Filter chain continues and downstream code can read the same ID.
- [ ] API errors include the resolved ID when the error envelope supports it.
