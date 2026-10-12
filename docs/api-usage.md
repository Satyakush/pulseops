# API usage

All application routes use the `/api/v1` prefix. Operational endpoints are protected by bearer-token authentication and role permissions.

## 1. Register or log in
Registration creates a `VIEWER` account:

```bash
curl -i -X POST http://localhost:8080/api/v1/auth/register \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo-viewer","password":"replace-this-password"}'
```

Log in to receive a bearer token:

```bash
curl -i -X POST http://localhost:8080/api/v1/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"demo-viewer","password":"replace-this-password"}'
```

Use the returned token as `Authorization: Bearer <token>` on protected requests. Do not paste real tokens into issue reports or commit them to source control.

## 2. Read services
```bash
curl -H 'Authorization: Bearer <token>' \
  http://localhost:8080/api/v1/services
curl -H 'Authorization: Bearer <token>' \
  'http://localhost:8080/api/v1/services?status=DEGRADED'
curl -H 'Authorization: Bearer <token>' \
  http://localhost:8080/api/v1/services/summary
```

## 3. Create a service
Requires a role with service-write permission:

```bash
curl -i -X POST http://localhost:8080/api/v1/services \
  -H 'Authorization: Bearer <token>' \
  -H 'Content-Type: application/json' \
  -d '{"name":"payments-api","description":"Processes customer payments"}'
```

## 4. Create an incident
```bash
curl -i -X POST http://localhost:8080/api/v1/incidents \
  -H 'Authorization: Bearer <token>' \
  -H 'Content-Type: application/json' \
  -d '{"serviceId":"00000000-0000-0000-0000-000000000001","title":"API latency","description":"Latency increased","severity":"HIGH"}'
```

The referenced service ID must identify a service that exists in the current repository state. New incidents start in `OPEN` status.

## Authorization overview
- `VIEWER`: read service and incident data.
- `OPERATOR`: read and write service and incident data.
- `ADMIN`: operator capabilities plus service deletion.

A `401` indicates missing or invalid authentication; a `403` indicates an authenticated identity lacks the required permission; a `400` indicates invalid request data; and a `404` indicates the requested resource was not found.


### Update incident details

Use `PATCH /api/v1/incidents/{id}` to edit an incident's title, description, and severity without changing its lifecycle status or creation timestamp.

```http
PATCH /api/v1/incidents/{id}
Content-Type: application/json

{
  "title": "Payment queue backlog",
  "description": "Queue depth exceeded the operational threshold.",
  "severity": "CRITICAL"
}
```

The title must be non-blank and at most 150 characters; the description is limited to 1,000 characters. Severity must be one of `LOW`, `MEDIUM`, `HIGH`, or `CRITICAL`. Successful edits invalidate incident caches and publish an `INCIDENT_UPDATED` event to the live event stream.


### Filter incidents

`GET /api/v1/incidents` supports optional `filter=ACTIVE|RESOLVED`, `severity=LOW|MEDIUM|HIGH|CRITICAL`, and `serviceId=<UUID>` query parameters. Filters can be combined, and results remain ordered newest first.

```http
GET /api/v1/incidents?filter=ACTIVE&severity=CRITICAL&serviceId=00000000-0000-0000-0000-000000000011
```


### Paginated incident search

Use `GET /api/v1/incidents/page` for bounded result sets. It accepts `page` (zero-based, default `0`) and `size` (default `20`, maximum `100`), plus the optional `filter`, `severity`, and `serviceId` filters. The response includes `items`, `page`, `size`, `totalElements`, and `totalPages`. PostgreSQL executes filtering, ordering, and pagination in the database; the in-memory profile preserves the same response contract.
