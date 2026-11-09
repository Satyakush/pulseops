# Incident Pagination Runbook

## Endpoint

Use `GET /api/v1/incidents/page` to retrieve incident records in bounded pages.

Supported query parameters:

- `page`: zero-based page index; defaults to `0`.
- `size`: requested page size; defaults to `20` and must be between `1` and `100`.
- `filter`: optional incident status filter (`ACTIVE` or `RESOLVED`).
- `severity`: optional incident severity.
- `serviceId`: optional service UUID.

A successful response includes `items`, `page`, `size`, `totalElements`, and `totalPages`.

## Expected validation

| Input | Expected result |
|---|---|
| Negative page index | HTTP 400, `page must be zero or greater` |
| Page size below 1 | HTTP 400, `size must be between 1 and 100` |
| Page size above 100 | HTTP 400, `size must be between 1 and 100` |
| Valid page and size | HTTP 200 with page metadata |

## Troubleshooting

1. Check the response status and error message before retrying.
2. Confirm the client uses a zero-based page index.
3. Keep page size at or below 100; iterate through pages instead of requesting a very large result set.
4. Use the optional filters to narrow results and reduce unnecessary data transfer.
5. If the returned page is empty, inspect `totalElements` and verify the filter values before treating it as an incident-data failure.

## Release checks

- [ ] Negative page indices are rejected with HTTP 400.
- [ ] Page sizes outside 1–100 are rejected with HTTP 400.
- [ ] Valid pagination returns consistent page metadata.
- [ ] Filters are applied alongside pagination.

## Incident activity timeline pagination

Use `GET /api/v1/incidents/{incidentId}/activity/page` to retrieve a bounded page of an incident's audit timeline.

Supported query parameters:

- `page`: zero-based page index; defaults to `0`.
- `size`: page size; defaults to `20` and must be between `1` and `100`.
- `action`: optional activity action filter when supported by the endpoint.

The response contains `items`, `page`, `size`, `totalElements`, and `totalPages`. An empty `items` array on a page beyond the last page is valid; inspect `totalElements` before treating it as an error.

Expected behavior:

| Scenario | Expected result |
|---|---|
| Unknown incident ID | HTTP 404 |
| Negative page index | HTTP 400 |
| Page size below 1 or above 100 | HTTP 400 |
| Valid page beyond last page | HTTP 200 with an empty `items` array |
| Valid request | HTTP 200 with pagination metadata |

For a support investigation, capture the incident ID, page, size, response status, and `X-Request-Id` header. Do not include authentication tokens in logs or support tickets.
