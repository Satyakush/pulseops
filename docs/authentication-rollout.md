# Authentication rollout checklist

- [ ] Replace in-memory users and sessions with durable persistence before production.
- [ ] Add database uniqueness constraints for normalized usernames.
- [ ] Configure HTTPS and trusted proxy headers.
- [ ] Rate-limit registration and login endpoints.
- [ ] Add password reset and account recovery workflows.
- [ ] Add audit events for login, logout, and role changes.
- [ ] Configure token/session invalidation during password changes.
- [ ] Verify access rules with integration tests for every HTTP method.
- [ ] Keep health probes public while protecting operational endpoints.
- [ ] Verify malformed query parameters and enum filters return HTTP 400 with a safe error envelope.
- [ ] Verify unexpected exceptions return generic client messages without leaking exception text or secrets.
- [ ] Confirm malformed UUID path parameters return HTTP 400 and do not leak conversion exceptions.
- [ ] Confirm unsupported enum filters return HTTP 400 rather than HTTP 500.
- [ ] Confirm validation errors include the request ID when available and never include stack traces or secrets.
