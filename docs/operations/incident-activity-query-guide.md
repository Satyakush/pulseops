# Incident Activity Query Guide

## Basic timeline

```http
GET /api/v1/incidents/{incidentId}/activity/page?page=0&size=20
Authorization: Bearer <token>
```

The endpoint uses zero-based pages. Keep `size` in the supported range of 1–100. The response should be treated as a snapshot of matching events at query time; subsequent incident changes may add new activity records.

## Filter by action

```http
GET /api/v1/incidents/{incidentId}/activity/page?page=0&size=20&action=STATUS_CHANGED
```

The documented action values are `CREATED`, `DETAILS_UPDATED`, and `STATUS_CHANGED`. Filtering should occur before pagination so that the total count and page contents describe the same filtered set.

## Interpreting results

- HTTP 200 with items: matching activity records were returned.
- HTTP 200 with an empty items list: the query was valid but no records matched that page/filter.
- HTTP 400: correct the page, size, action, or identifier format before retrying.
- HTTP 404: confirm the incident ID and environment.
- HTTP 500: report the request ID and investigate server-side logs; do not retry malformed input unchanged.

## Operational notes

Use a small page size for interactive timelines and paginate when the result indicates more records. Do not assume an empty page means the incident has never had activity until you check the page index, action filter, and total count.
