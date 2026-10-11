# PulseOps

PulseOps is a production-oriented operations platform built to demonstrate modern backend engineering with Java and Spring Boot.

## Current stack
- Java 21
- Spring Boot 3
- Maven
- Spring Security, Jakarta Validation, Spring Data JPA
- H2 for local development; PostgreSQL profile/configuration available
- Optional Redis caching
- Server-Sent Events (SSE) for live operational events

## Run locally

Requirements: JDK 21 and Maven (or use the Maven wrapper).

The default profile uses an in-memory H2 database. The configured demo accounts read their passwords from environment variables, so set them before starting the app:

```bash
export PULSEOPS_VIEWER_PASSWORD='replace-with-a-local-password'
export PULSEOPS_OPERATOR_PASSWORD='replace-with-a-local-password'
export PULSEOPS_ADMIN_PASSWORD='replace-with-a-local-password'
./mvnw spring-boot:run
```

On Windows PowerShell, use `$env:PULSEOPS_VIEWER_PASSWORD = '...'` (and likewise for the operator and admin variables) before running `./mvnw spring-boot:run`.

Run the same verification command used by CI:

```bash
./mvnw --batch-mode --no-transfer-progress verify
```

## Authentication and authorization

- `POST /api/v1/auth/register` creates a `VIEWER` account; registration never returns a password hash.
- `POST /api/v1/auth/login` validates credentials and returns a bearer token.
- `GET /api/v1/auth/me` returns the authenticated identity and role.
- `POST /api/v1/auth/logout` revokes the current in-memory session.
- Send protected requests with `Authorization: Bearer <token>`.

Roles:
- **VIEWER** — read service and incident data.
- **OPERATOR** — read and write service and incident data.
- **ADMIN** — operator capabilities plus service deletion.

Registration and login are public; operational API routes require the relevant permission. The current development user and session repositories are in-memory and are **not suitable for production** because data is lost on restart. See [the security model](docs/security-model.md) and [the authentication rollout notes](docs/authentication-rollout.md) for limitations and hardening requirements.

## API reference

### Health and operations
- `GET /api/v1/health` — application health response with request correlation ID.
- `GET /actuator/health`
- `GET /actuator/info`
- `GET /actuator/metrics`
- `GET /api/v1/events` — subscribes to the `text/event-stream` feed.
- `GET /api/v1/cache/metrics` — cache invalidation counters.

### Services
- `GET /api/v1/services` — list services, ordered by name.
- `GET /api/v1/services?status=DEGRADED` — filter by status.
- `GET /api/v1/services/{id}` — retrieve one service.
- `GET /api/v1/services/summary` — total, operational, degraded, and outage counts.
- `POST /api/v1/services` — create a service; requires write permission.
- `PATCH /api/v1/services/{id}/status` — update status; requires write permission.
- `DELETE /api/v1/services/{id}` — delete a service; requires admin permission.

Create-service request:

```json
{
  "name": "payments-api",
  "description": "Processes customer payments"
}
```

### Incidents
- `GET /api/v1/incidents` — list incidents.
- `GET /api/v1/incidents?filter=ACTIVE` — filter incidents.
- `GET /api/v1/incidents/{id}` — retrieve one incident.
- `GET /api/v1/incidents/summary` — counts by incident status.
- `POST /api/v1/incidents` — create an incident; requires write permission.
- `PATCH /api/v1/incidents/{id}/status` — update incident status; requires write permission.

Create-incident request:

```json
{
  "serviceId": "00000000-0000-0000-0000-000000000001",
  "title": "API latency",
  "description": "Latency increased above the normal threshold",
  "severity": "HIGH"
}
```

### Authentication example

After logging in, use the returned token for protected endpoints:

```bash
curl --header "Authorization: Bearer <token>" \
  http://localhost:8080/api/v1/services
```

## Caching

PulseOps includes an opt-in Redis cache layer for high-frequency service and incident reads.

- Service lists, status-filtered lists, and summaries are cacheable.
- Incident lists, filtered lists, and summaries are cacheable.
- Service and incident mutations evict corresponding cache entries.
- Cached values use JSON serialization with a 60-second default TTL.
- Redis caching is disabled by default; enable `pulseops.cache.redis.enabled` when Redis is available.
- The cache health indicator contributes cache details to Actuator health.

## CI

GitHub Actions runs `./mvnw --batch-mode --no-transfer-progress verify` on pushes to `main` and pull requests targeting `main`. A green run applies only to the exact commit SHA shown on the workflow run; after merging, check the new `main` run as well.
