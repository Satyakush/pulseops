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
