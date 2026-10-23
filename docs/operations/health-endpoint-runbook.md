# Health Endpoint Runbook

## Endpoint contract

- **Route:** `GET /api/v1/health`
- **Healthy response:** HTTP `200 OK` with `status: UP` and `service: pulseops`.
- **Metadata:** The response includes a timestamp and the resolved request correlation ID when the request passed through the request ID filter.
- **Scope:** This endpoint is a lightweight application liveness signal. Do not interpret `UP` as proof that every downstream dependency is reachable.

## Triage

1. Call `GET /api/v1/health` and confirm the HTTP status and `status` field.
2. Capture the `requestId` value and search application logs using that exact value.
3. If the endpoint is unavailable, inspect application startup logs and the health controller's request path.
4. If the response succeeds but an operation fails, check the relevant dependency-specific logs and health indicators rather than relying on this endpoint alone.

## Release checks

- [ ] Endpoint returns HTTP 200 and `status: UP` when the application is serving requests.
- [ ] Response identifies the service as `pulseops`.
- [ ] Response includes a timestamp.
- [ ] Request correlation ID is included when available in request context.
- [ ] Documentation does not imply that liveness confirms database, Redis, or other dependency readiness.
