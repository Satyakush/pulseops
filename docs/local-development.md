# Local development

## Prerequisites
- JDK 21
- Git
- The repository Maven wrapper (`./mvnw`; on Windows use `mvnw.cmd`)

## Start the application
The default Spring profile configures an in-memory H2 database. Demo credentials are intentionally supplied through environment variables rather than committed as literal passwords.

macOS/Linux:

```bash
export PULSEOPS_VIEWER_PASSWORD='choose-a-local-password'
export PULSEOPS_OPERATOR_PASSWORD='choose-a-local-password'
export PULSEOPS_ADMIN_PASSWORD='choose-a-local-password'
./mvnw spring-boot:run
```

Windows PowerShell:

```powershell
$env:PULSEOPS_VIEWER_PASSWORD = 'choose-a-local-password'
$env:PULSEOPS_OPERATOR_PASSWORD = 'choose-a-local-password'
$env:PULSEOPS_ADMIN_PASSWORD = 'choose-a-local-password'
./mvnw.cmd spring-boot:run
```

The server listens on port 8080 by default. Check `GET /api/v1/health` or `GET /actuator/health` after startup.

## Verify changes before pushing
Run the same lifecycle as CI:

```bash
./mvnw --batch-mode --no-transfer-progress verify
```

If a test fails, inspect the first failing assertion and full Maven cause before changing code. Avoid repeatedly pushing speculative test fixes: run the targeted test first, then rerun the full verification suite.

## Profiles and external services
- Default/local development uses H2 and disables Redis caching.
- The PostgreSQL profile is intended for a configured PostgreSQL instance; provide database connection values through the deployment environment.
- Redis is optional. Enable `pulseops.cache.redis.enabled` only when a reachable Redis service is available.

Do not use the in-memory user/session repositories or demo accounts as a production identity store. See [the security model](security-model.md) and [authentication rollout notes](authentication-rollout.md).
