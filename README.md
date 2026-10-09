# PulseOps

PulseOps is a production-oriented operations platform built to demonstrate modern backend engineering with Java and Spring Boot.

## Current Stack
- Java 21
- Spring Boot
- Maven

## Roadmap
1. Application foundation
2. Health and observability
3. PostgreSQL persistence
4. Authentication and RBAC
5. Incident and service management
6. Real-time updates
7. Redis caching
8. Testing and API documentation
9. Docker and CI/CD
10. Production hardening


## Security

PulseOps uses stateless bearer-token authentication with an eight-hour session expiry.

### Authentication
- `POST /api/v1/auth/register` creates a `VIEWER` account; registration never returns a password hash.
- `POST /api/v1/auth/login` validates credentials and returns an opaque bearer token.
- `GET /api/v1/auth/me` returns the authenticated username and role.
- `POST /api/v1/auth/logout` revokes the current in-memory session.
- Send protected requests with `Authorization: Bearer <token>`.

### Roles
- VIEWER: read service and incident data.
- OPERATOR: read and write service and incident data.
- ADMIN: operator capabilities plus service deletion.

Registration and login are public; operational API routes require the relevant permission. The development user and session repositories are in-memory and are **not suitable for production** because data is lost on restart. See [docs/security-model.md](docs/security-model.md) and [docs/authentication-rollout.md](docs/authentication-rollout.md) for limitations and production hardening.

## API

### Services
- GET /api/v1/services
- GET /api/v1/services?status=DEGRADED
- GET /api/v1/services/summary
- POST /api/v1/services
- PATCH /api/v1/services/{id}/status
- DELETE /api/v1/services/{id}

Service listings use case-insensitive name ordering. The summary endpoint reports total, operational, degraded, and outage counts.

### Incidents
- GET /api/v1/incidents
- GET /api/v1/incidents/{id}
- POST /api/v1/incidents

Create an incident with serviceId, title, severity, and an optional description. New incidents start in OPEN status and are returned newest-first.

Example request:

POST /api/v1/incidents

{
  "serviceId": "00000000-0000-0000-0000-000000000001",
  "title": "API latency",
  "description": "Latency increased",
  "severity": "HIGH"
}

## Development
Run the application with:

./mvnw spring-boot:run

## Real-time operations events

PulseOps now exposes a server-sent events stream for live operational changes:

- `GET /api/v1/events` — subscribes to a `text/event-stream` feed.
- Service creation, deletion, and status changes emit typed events.
- Incident creation and status changes emit typed events.
- Events are published through Spring application events, keeping domain services decoupled from the transport layer.
- The stream tracks active subscribers and removes completed or failed connections.

The current event payload includes an event id, event type, resource type, resource id, and occurrence timestamp. Event types are represented by a dedicated enum to keep producers and consumers aligned; a heartbeat is also emitted every 30 seconds while subscribers are connected. This provides a foundation for a future operations dashboard without coupling the backend to a specific frontend implementation.

## Observability

Actuator endpoints are exposed for operational monitoring:

- `/actuator/health`
- `/actuator/info`
- `/actuator/metrics`

Health remains publicly readable for platform probes, while the application API remains protected by the existing RBAC policy.

## Redis caching

PulseOps includes an opt-in Redis cache layer for high-frequency service and incident reads.

- Service lists, status-filtered lists, and summaries are cacheable.
- Incident lists, filtered lists, and summaries are cacheable.
- Service and incident mutations evict the corresponding cache entries.
- Cached values use JSON serialization with a 60-second default TTL.
- Redis caching is disabled by default for local development; enable `pulseops.cache.redis.enabled` when Redis is available.
- `/api/v1/cache/metrics` exposes cache invalidation counters.
- The cache health indicator contributes cache invalidation details to Actuator health.
