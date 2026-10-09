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
