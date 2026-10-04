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

PulseOps uses stateless HTTP Basic security for its API.

### Roles
- VIEWER: read service and incident data.
- OPERATOR: read data and create or update services and incidents.
- ADMIN: operator capabilities plus service deletion.

Development users:
- viewer / viewer
- operator / operator
- admin / admin

GET /api/v1/auth/me returns the authenticated username and role. Unauthenticated and forbidden API requests return JSON error responses.

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
