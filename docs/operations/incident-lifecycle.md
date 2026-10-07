# Incident Lifecycle

An incident follows a small, explicit lifecycle:

1. **Open** — an operational problem has been recorded.
2. **Investigating** — an operator is actively determining impact and cause.
3. **Resolved** — remediation has restored expected behavior.
4. **Closed** — the incident record no longer requires active follow-up.

Status transitions are validated at the API boundary and should remain consistent across REST responses, persistence, and event notifications.
