# Maven CI troubleshooting

PulseOps CI runs the Maven verification lifecycle on Java 21 for pushes to `main` and pull requests targeting `main`.

## First response to a red run

1. Open the failed workflow run and expand the **test** job.
2. Find the first failing Maven test or compilation error. Later failures may be consequences of the first failure.
3. Reproduce with the same Java major version: `./mvnw --batch-mode --no-transfer-progress verify`.
4. Fix the underlying behavior or test setup; do not repeatedly change unrelated assertions to make the run green.
5. Re-run the full verification lifecycle before merging and confirm the push to `main` also passes.

## Common failure classes

- **Compilation errors:** check the first compiler diagnostic and API signatures before changing tests.
- **Spring context failures:** inspect missing beans, profile activation, duplicate filter chains, and property placeholders.
- **MVC slice failures:** explicitly import only the required collaborators and keep each test slice focused on one controller.
- **Order-dependent tests:** avoid shared mutable fixtures; create fresh repositories and service objects per test.
- **Database failures:** verify the active profile and embedded database configuration. Do not assume a PostgreSQL profile is automatically provisioned in CI.
- **Flaky or obsolete runs:** check the head SHA. A newer successful run does not mean an older failure was repaired retroactively.

## Merge criteria

Merge only after the current branch head has a successful CI result and the default branch receives a successful push-triggered run. Record the failing SHA and the fix commit when documenting regressions.
