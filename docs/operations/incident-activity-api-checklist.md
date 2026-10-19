# Incident Activity API Verification Checklist

## Request validation
- [ ] Negative page indexes return HTTP 400.
- [ ] Page sizes outside 1–100 return HTTP 400.
- [ ] Unsupported activity action filters return HTTP 400, not HTTP 500.
- [ ] Malformed incident identifiers return a client error without leaking conversion exceptions.

## Pagination behavior
- [ ] Page indexes are zero-based.
- [ ] A valid query with no matching activity returns HTTP 200 and an empty items array.
- [ ] Missing incidents return HTTP 404.
- [ ] Pagination metadata agrees with the filtered result set.

## Safe errors and operations
- [ ] Error bodies use stable, client-safe messages.
- [ ] Request correlation IDs are preserved when available.
- [ ] Stack traces, secrets, and raw exception details are not returned to callers.
- [ ] For HTTP 500, operators use the correlation ID to locate server-side diagnostics.

## Release gate
Run the repository's CI verification on the exact proposed merge commit. Do not merge based only on a successful run for an older commit.
