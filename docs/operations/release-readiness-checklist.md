# PulseOps Release Readiness Checklist

## Code and tests
- [ ] The pull request has a clear scope and no unrelated generated files.
- [ ] Regression tests cover the behavior being changed, including invalid inputs and boundary values.
- [ ] Full Maven verification passes on the exact proposed head SHA.
- [ ] No unreviewed failing or skipped tests remain.

## API and security
- [ ] Authorization is enforced server-side for every protected operation.
- [ ] Error responses use stable, client-safe messages.
- [ ] Request IDs are bounded, safe for logs, and never treated as credentials.
- [ ] No secrets, personal data, or production credentials are committed.
- [ ] Validation and pagination limits are documented.

## Operations
- [ ] Health and metrics endpoints remain available.
- [ ] Configuration changes include safe defaults and environment-specific notes.
- [ ] Runbooks explain how to diagnose failures without exposing sensitive data.
- [ ] Rollback or mitigation steps are documented for risky behavior changes.

## Merge and aftercare
- [ ] Review the exact diff and commit history.
- [ ] Merge only when required checks pass.
- [ ] Verify the merge commit and main-branch CI afterward.
- [ ] Confirm documentation links and API examples match the current implementation.
