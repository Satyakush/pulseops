# CI failure triage

The Maven CI workflow runs `./mvnw --batch-mode --no-transfer-progress verify` on pushes to `main` and pull requests targeting `main`.

## Verify the exact run
1. Open the failed Actions run and note its branch and full commit SHA.
2. Confirm the run belongs to the latest commit on that branch. A successful run for an older SHA does not validate a newer change.
3. Open the `test` job and locate the first failing test or Maven error. Later failures can be cascading consequences.
4. Reproduce locally with the same Java major version (21): `./mvnw --batch-mode --no-transfer-progress verify`.
5. For a single failing test, use `./mvnw -Dtest=ClassNameTest test` first, then run the full verification command.
6. Fix the root cause, rerun verification, and push the correction. After merging, verify the push-triggered run on `main`.

## Common failure categories
- **Compilation error:** check the first compiler diagnostic and imports/types changed in the commit.
- **Application-context startup:** inspect the earliest bean/configuration exception and check whether a test slice needs a narrowly scoped test configuration.
- **Security test failure:** verify the expected authentication/authorization boundary; do not disable security globally to make a test pass.
- **Order-dependent tests:** remove shared mutable test state or reset it explicitly; do not weaken assertions to hide cross-test interference.
- **Environment-dependent test:** make the test independent of unavailable services, or use an explicit test profile. Redis is disabled by default for local development.

## Safe workflow habits
- Do not merge a pull request until its latest commit has a successful required CI run.
- Do not treat a cancelled run as either a pass or a code failure; inspect the replacement run.
- Do not repeatedly push speculative fixes without reading the failure output.
- Keep fixes narrow and add a regression test when practical.
- Never commit secrets, local credentials, build output, or generated test reports.
