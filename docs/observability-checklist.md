# PulseOps Observability Checklist

Before shipping an operational change, verify:

- Health endpoint returns `UP`.
- Request IDs are preserved across a request/response pair.
- Error payloads contain the request ID.
- Cache health is visible through the existing actuator surface.
- Security failures remain JSON responses.
- SSE clients receive heartbeats and stale emitters are removed.
