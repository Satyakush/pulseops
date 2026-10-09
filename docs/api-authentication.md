# Authentication API

## Register
`POST /api/v1/auth/register` with JSON `{ "username": "operator1", "password": "a-long-password" }`. Usernames must be 3–80 characters and passwords 8–120 characters. New accounts receive the `VIEWER` role. A successful response is `201 Created` and contains no password hash.

## Login
`POST /api/v1/auth/login` with username and password. A successful response contains an opaque bearer token, expiry timestamp, username, and role. Invalid credentials return `401 Unauthorized`.

## Current user
Send `Authorization: Bearer <token>` to `GET /api/v1/auth/me`.

## Logout
Send the same bearer header to `POST /api/v1/auth/logout`. The session is revoked; the response is `204 No Content`.

## Authorization
Service and incident routes require the relevant read/write permission. Unauthenticated requests to protected routes are rejected. In-memory user/session storage is development-only.
