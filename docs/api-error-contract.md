# API Error Contract

PulseOps error responses now carry a request correlation identifier.

## Shape

```json
{
  "timestamp": "2026-10-07T00:00:00Z",
  "status": 404,
  "error": "Not Found",
  "message": "Service not found",
  "path": "/api/v1/services/...",
  "requestId": "..."
}
```

## Correlation

Clients may send `X-Request-Id`. If omitted, PulseOps generates one. The same value is returned in the response header and included in handled API errors.

This makes support/debugging workflows traceable without exposing authentication credentials or internal stack traces.
