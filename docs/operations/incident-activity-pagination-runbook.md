# Incident activity pagination runbook

## Expected behavior

Use the paginated endpoint for long timelines:

`GET /api/v1/incidents/{incidentId}/activity/page?page=0&size=20`

Optional `action` filters must use a supported action enum. Page indexes are zero-based and page sizes must be between 1 and 100.

## Triage by response

| HTTP status | Meaning | Operator action |
| --- | --- | --- |
| 200 with items | Matching activity exists | Inspect timestamps and actions in newest-first order. |
| 200 with empty items | Incident exists, no activity matches the filter | Remove the filter or inspect a different page. |
| 400 | Invalid page, size, or action parameter | Correct the request; do not retry unchanged input. |
| 404 | Incident does not exist | Verify the incident ID and caller's intended environment. |
| 500 | Unexpected server failure | Use the request correlation ID to locate server-side logs; do not expose internal exception details to clients. |

## Regression checklist

- Negative page index returns 400.
- Size greater than 100 returns 400.
- Unsupported action enum returns 400, never 500.
- A valid filter with no matching entries returns 200 with zero totals.
- Unknown incident ID returns 404.
- Error responses retain request correlation IDs when present and use safe messages.
