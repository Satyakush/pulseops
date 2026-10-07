# Observability Metrics

PulseOps observability should answer three questions quickly:

1. Is the application healthy?
2. Is request latency increasing?
3. Are failures concentrated around a particular endpoint or dependency?

The health endpoint provides a lightweight availability signal. Request ids provide traceability for individual calls. Cache metrics expose cache behavior without making cache state part of the API contract.

When investigating an incident, correlate the request id, endpoint, HTTP status, and timestamp before changing application behavior.
