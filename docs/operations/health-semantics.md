# Health Endpoint Semantics

The health endpoint is intentionally lightweight and safe to query without authentication.

Its response is typed so clients can rely on a stable contract instead of parsing arbitrary maps. The response also exposes the request correlation identifier, allowing health checks and diagnostic calls to be tied back to server-side observations.

Health indicates application availability; it should not be treated as a complete business-data consistency check.
