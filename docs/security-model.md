# Security model

- Passwords are stored as BCrypt hashes; raw passwords are never returned from registration.
- Login creates an eight-hour opaque bearer token.
- The application validates bearer sessions on protected requests and rejects expired, revoked, disabled-user, or unknown-user sessions.
- `VIEWER` has read permissions; `OPERATOR` can read and write service and incident resources; `ADMIN` has all defined permissions.
- Registration and login are public. Business API routes require the corresponding permission authority.
- This in-memory session/user implementation is suitable for local development only; production deployments must persist users and sessions and configure TLS, rate limits, secret management, and account recovery.

- API error responses use stable, client-safe messages for unexpected failures; exception details and secrets must remain in server-side diagnostics, not response bodies.
- Invalid query-parameter types and enum values are treated as client errors (HTTP 400), not server failures.

- Invalid typed query/path parameters (including malformed UUIDs and unknown enum values) must return HTTP 400 with a stable, client-safe message; never serialize exception messages or stack traces into the response.
- Error responses should preserve the request correlation ID when one was assigned so operators can find server-side diagnostics without exposing them to callers.
