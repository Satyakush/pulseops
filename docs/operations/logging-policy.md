# Logging Policy

Application logs should provide enough context to diagnose a request without leaking credentials or sensitive payloads.

For request-level investigation, prefer structured context such as:

- request id
- HTTP method
- route
- status
- timestamp
- relevant resource identifier

Never log passwords, authentication headers, or raw secrets. When an error is returned to a client, the request id should be sufficient to locate the corresponding server-side diagnostic context.
