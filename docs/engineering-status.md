# Engineering status

## Authentication baseline
- Registration stores BCrypt password hashes and returns a safe user projection.
- Login issues an opaque bearer token with an eight-hour expiry.
- Protected requests resolve sessions and require service/incident permissions.
- Logout revokes the in-memory session.
- Service deletion is restricted to the ADMIN role.

## Known limitations before production
- User and session repositories are in-memory, so accounts and sessions are lost on restart.
- No durable account uniqueness constraint, password recovery, login throttling, or audit trail is implemented yet.
- The security filter and endpoint authorization need integration coverage against the running Spring context.
- Run mvn --batch-mode --no-transfer-progress verify before treating a change as release-ready.

## Next engineering priorities
1. Persist users and sessions with database constraints.
2. Add security integration tests for anonymous, VIEWER, OPERATOR, and ADMIN requests.
3. Add token rotation and revocation semantics for password changes.
4. Add API schema generation and containerized local development.
