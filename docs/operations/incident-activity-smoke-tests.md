# Incident Activity API Smoke Tests

Run these checks against a local PulseOps instance after setting the required demo credentials and starting the application.

## Verify request validation

1. Authenticate with an operator account and retain the bearer token.
2. Create an incident and save its returned ID.
3. Request `GET /api/v1/incidents/{id}/activity/page?page=-1`; expect HTTP 400.
4. Request `GET /api/v1/incidents/{id}/activity/page?size=101`; expect HTTP 400.
5. Request `GET /api/v1/incidents/{id}/activity/page?action=UNKNOWN_ACTION`; expect HTTP 400, never HTTP 500.
6. Request the incident endpoint with a malformed identifier; expect a client error and a safe response body.
7. Request a valid action filter with no matching events; expect HTTP 200 and an empty items array.
8. Request an unknown incident ID; expect HTTP 404.

## Verify response safety

- Confirm errors use the documented API error envelope.
- Confirm response bodies do not expose stack traces, SQL details, secrets, or raw exception messages.
- Capture the request correlation ID and verify it can be used to locate server-side diagnostics.

## Release check

Run `./mvnw --batch-mode --no-transfer-progress verify` and review the workflow for the exact head SHA before merging. Re-run verification after any subsequent source or test change.
