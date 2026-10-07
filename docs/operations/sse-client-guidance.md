# SSE Client Guidance

PulseOps event streaming uses Server-Sent Events for one-way operational updates.

Clients should:

- Keep the connection open while the dashboard is active.
- Handle heartbeat events without treating them as business events.
- Reconnect after transient network failures.
- Avoid opening duplicate streams for the same dashboard session.
- Treat event payloads as notifications and re-fetch authoritative resource state when necessary.

The server-side stream improves responsiveness while the database remains the source of truth.
