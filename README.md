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

### List services

`GET /api/v1/services`

Optional status filtering:

`GET /api/v1/services?status=DEGRADED`

Results are returned in case-insensitive name order.

### Create a service

`POST /api/v1/services`

```json
{
  "name": "payments-api",
  "description": "Payment processing API"
}
```

## Development
Run the application with:

```bash
./mvnw spring-boot:run
```
