# Incident Activity Timeline

The incident activity timeline is an append-only audit trail for meaningful incident lifecycle changes.

## API

`GET /api/v1/incidents/{incidentId}/activity` returns the incident's activity entries in newest-first order. The endpoint first verifies that the incident exists, so an unknown incident returns the standard API 404 response rather than an indistinguishable empty list.

Each entry contains:
- `id`: unique activity identifier
- `incidentId`: incident identifier
- `action`: stable action code (`CREATED`, `DETAILS_UPDATED`, or `STATUS_CHANGED`)
- `actor`: authenticated principal when available, otherwise `system`
- `summary`: concise human-readable description
- `occurredAt`: timestamp with offset

## Recording rules

- Creating an incident records `CREATED`.
- Updating incident details records `DETAILS_UPDATED`.
- Changing incident status records `STATUS_CHANGED`.
- Activity entries are written through the same application service as the lifecycle operation and are not editable through the public API.
- The in-memory repository is used by default; the `postgres` profile persists entries in `incident_activity`, indexed by incident and timestamp.

## Operational notes

The timeline is an audit aid, not a replacement for application logs or event delivery. Keep summaries free of secrets, credentials, and unnecessary personal data. Before a production rollout, verify that incident writes and activity writes share a transaction under the selected persistence profile.
