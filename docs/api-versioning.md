# API Versioning

PulseOps routes are currently namespaced under `/api/v1`.

The `application/vnd.pulseops.v1+json` media type is a stable contract marker for clients that need explicit content negotiation. A future API version should preserve v1 routes until a documented migration path is available.
