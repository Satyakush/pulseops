# API Error Response Runbook

## Error envelope

PulseOps uses a consistent `ApiError` response for handled API failures:

- `timestamp`: time the error envelope was created.
- `status` and `error`: HTTP status code and reason phrase.
- `message`: client-safe summary; unexpected exceptions must not expose stack traces, credentials, or raw internal exception messages.
- `path`: request URI.
- `requestId`: resolved correlation ID when the request ID filter has populated request context.

## Common responses

| Situation | Expected status | Client-facing behavior |
|---|---:|---|
| Resource not found | 404 | Return a resource-specific not-found message. |
| Invalid request values or unreadable body | 400 | Return a safe validation/parsing message. |
| Invalid credentials | 401 | Use the generic message `Invalid username or password`; do not reveal whether a username exists. |
| Duplicate resource or invalid lifecycle transition | 409 | Explain the conflict without leaking internal details. |
| Unexpected exception | 500 | Return `An unexpected error occurred`; do not echo the exception message. |

## Triage

1. Capture the HTTP status, response body, and `requestId`.
2. Search application logs using the correlation ID; never treat it as an authentication credential.
3. For 400 responses, inspect request shape and validation failures without logging secrets.
4. For 401 responses, verify the submitted credentials through the normal authentication flow; do not infer account existence from the message.
5. For 409 responses, check the current resource state and documented transition rules.
6. For 500 responses, investigate server-side logs and stack traces in restricted logs, not in the client response.

## Release checks

- [ ] Handled errors include status, message, path, timestamp, and request ID when available.
- [ ] Unexpected exceptions return a generic 500 message.
- [ ] Invalid credentials use the same generic client-facing message.
- [ ] Malformed request bodies return a safe 400 response.
- [ ] Error responses preserve request correlation context.
