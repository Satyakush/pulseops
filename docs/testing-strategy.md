# Testing Strategy

PulseOps keeps fast unit and MVC tests close to the domain boundaries.

## Current layers

- **Controller tests** verify HTTP status codes, validation, serialization, and endpoint behavior.
- **Security tests** verify role-based access decisions.
- **Component tests** cover request correlation and API contracts.
- **Cache tests** verify cache policy, metrics, and health behavior.

## Rule

A behavior change should add or update a focused test at the narrowest useful layer. Tests should avoid real external Redis or PostgreSQL dependencies unless an integration test explicitly requires them.
