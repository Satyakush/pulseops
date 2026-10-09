# Security model

- Passwords are stored as BCrypt hashes; raw passwords are never returned from registration.
- Login creates an eight-hour opaque bearer token.
- The application validates bearer sessions on protected requests and rejects expired, revoked, disabled-user, or unknown-user sessions.
- `VIEWER` has read permissions; `OPERATOR` can read and write service and incident resources; `ADMIN` has all defined permissions.
- Registration and login are public. Business API routes require the corresponding permission authority.
- This in-memory session/user implementation is suitable for local development only; production deployments must persist users and sessions and configure TLS, rate limits, secret management, and account recovery.
