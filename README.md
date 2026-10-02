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
