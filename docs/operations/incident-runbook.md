# Incident Response Runbook

When PulseOps reports unexpected behavior:

## 1. Establish scope
Identify the affected endpoint, user action, timestamp, and HTTP status.

## 2. Correlate
Capture the `X-Request-Id` from the failing request or API error.

## 3. Check health
Confirm the health endpoint and application availability.

## 4. Check dependencies
Inspect persistence and cache behavior before changing application code.

## 5. Remediate
Apply the smallest safe change and verify the affected flow.

## 6. Record
Document impact, cause, remediation, and follow-up work in the incident record.
