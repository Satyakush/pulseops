# Incident Activity Timeline

The incident activity timeline is an append-only audit trail for meaningful incident lifecycle changes.

## API

`GET /api/v1/incidents/{incidentId}/activity` returns the incident's activity entries in newest-first order. The endpoint first verifies that the incident exists, so an unknown incident returns the standard API 404 response rather than an indistinguishable empty list. For long histories, use `GET /api/v1/incidents/{incidentId}/activity/page?page=0&size=20`; page numbering is zero-based and page size is limited to 1–100. Add `action=CREATED`, `action=DETAILS_UPDATED`, or `action=STATUS_CHANGED` to filter the page by event type. Filtering is applied before pagination and totals are calculated only over matching entries. The paginated response includes `items`, `page`, `size`, `totalElements`, and `totalPages`, with PostgreSQL applying the filter, limit, and offset in the database.

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


## Pagination error responses

The paginated activity endpoint uses the standard `ApiError` response envelope:

- `400 Bad Request`: page is negative, size is outside 1–100, or the action filter is not a supported `IncidentActivityAction`.
- `404 Not Found`: the incident identifier does not resolve to an existing incident.
- `200 OK` with an empty `items` array: the incident exists, but no activity entries match the requested filter/page.

Invalid query values are client errors. They must not be reported as internal server errors, and responses must not expose exception details or stack traces.
