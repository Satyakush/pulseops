# Authentication and Roles

PulseOps uses stateless HTTP authentication with explicit application roles.

## Roles

- **VIEWER** — read operational resources.
- **OPERATOR** — read and modify services and incidents.
- **ADMIN** — perform operator actions plus administrative operations such as deletion.

Authorization belongs at the route boundary so protected operations cannot accidentally become reachable through an unprotected controller method.
