# Pre-merge verification checklist

Use this checklist for every PulseOps change that affects application code, tests, configuration, or CI.

## Local verification

- [ ] Use Java 21, matching the CI runtime.
- [ ] Run `./mvnw --batch-mode --no-transfer-progress verify` from the repository root.
- [ ] Confirm the first failure is understood; do not mask it by weakening unrelated assertions.
- [ ] For persistence changes, run repository tests and verify the selected Spring profile.
- [ ] For API changes, verify success status, validation errors, not-found behavior, and authorization boundaries.
- [ ] For authentication changes, verify invalid credentials, expired sessions, disabled users, and token revocation.
- [ ] For caching changes, verify mutation invalidation and disabled-by-default local behavior.

## GitHub Actions verification

- [ ] Confirm the workflow run is attached to the latest branch SHA.
- [ ] Confirm the Maven test job completed successfully.
- [ ] Review the diff for accidental secrets, generated output, and unrelated formatting changes.
- [ ] After merge, confirm the push-triggered run on `main` succeeds.

A green run for an earlier commit does not validate a later commit. If CI fails, preserve the first failure log and fix the cause before proceeding.
