# Authentication

PulseOps authentication uses BCrypt password hashes and short-lived application sessions. Registration creates an ACTIVE VIEWER by default. Roles are ADMIN, OPERATOR, and VIEWER. Authentication endpoints are `POST /api/v1/auth/register` and `POST /api/v1/auth/login`.
