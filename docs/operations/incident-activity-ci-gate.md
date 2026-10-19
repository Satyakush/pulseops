# Incident Activity CI Release Gate

Before merging changes to incident activity pagination or error handling:

1. Confirm the workflow ran for the current pull-request head SHA.
2. Confirm compilation and the full Maven verification job passed.
3. Review regression coverage for empty filtered results, negative pages, oversized page sizes, unsupported enum values, malformed incident IDs, actor fallback/truncation, and summary length limits.
4. Confirm invalid client input produces HTTP 400 rather than HTTP 500.
5. Confirm safe error responses do not disclose stack traces or internal exception details.
6. If any commit is pushed after a green run, wait for CI to verify the new head before merging.

A green run on an earlier commit is not evidence that the latest head is green.
