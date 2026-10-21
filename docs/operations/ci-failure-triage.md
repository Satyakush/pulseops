# CI Failure Triage Workflow

## First five minutes

1. Open the failed GitHub Actions run and note the exact commit SHA, workflow, job, and failing step.
2. Read the first actionable compiler/test failure, not only the final Maven summary.
3. Classify it as compilation, unit test, integration test, formatting, dependency/network, or environment failure.
4. Reproduce with the same command as CI: `./mvnw --batch-mode --no-transfer-progress verify`.
5. Fix the underlying behavior or test expectation; avoid weakening assertions solely to make the build green.

## Regression discipline

- Add a focused test for a reproducible defect.
- Run the narrow test first, then the full verification suite.
- Inspect the diff for unrelated changes and accidental secrets.
- Push the fix and verify the workflow against the new head SHA. A green result for an earlier commit is stale evidence.
- If a workflow cannot be triggered or its status cannot be retrieved, report verification as unknown rather than passing.

## Common Spring Boot failures

- Compilation: inspect imports, method signatures, and test fixture APIs.
- MockMvc mismatch: compare actual status/body to the controller advice contract.
- Persistence tests: inspect transaction boundaries, repository ordering, and test database configuration.
- Flaky asynchronous tests: wait on observable conditions with bounded timeouts rather than arbitrary sleeps.
