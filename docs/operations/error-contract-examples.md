# API Error Contract Examples

PulseOps returns a consistent error envelope for expected API failures.

A typical response contains:

- `status`: HTTP status code
- `message`: client-safe description
- `timestamp`: server-side occurrence time
- `requestId`: correlation identifier

## Example

```json
{
  "status": 400,
  "message": "name is required",
  "timestamp": "2026-10-07T19:00:00Z",
  "requestId": "8f8d2b4a"
}
```

Clients should log the request id when reporting failures so operators can trace the request through application logs.
