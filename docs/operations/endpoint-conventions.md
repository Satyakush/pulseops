# API Endpoint Conventions

PulseOps exposes versioned HTTP APIs under `/api/v1`.

## Conventions

- Use nouns for resource paths.
- Use HTTP methods to express the requested operation.
- Return validation failures through the shared API error contract.
- Preserve `X-Request-Id` across successful and failed requests.
- Keep authentication and authorization decisions in Spring Security configuration.
- Keep business rules inside application services rather than controllers.

These conventions make the API predictable for both browser clients and service-to-service consumers.
