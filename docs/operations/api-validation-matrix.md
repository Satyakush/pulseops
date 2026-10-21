# API Validation Regression Matrix

Use this matrix when changing request validation or global exception handling.

| Input class | Expected behavior | Regression check |
| --- | --- | --- |
| Missing required field | Client validation error | Assert 4xx and field-safe message |
| Invalid enum query value | HTTP 400 | Ensure it never becomes HTTP 500 |
| Malformed UUID path value | HTTP 400 | Assert safe response envelope |
| Negative page index | HTTP 400 | Check documented message |
| Page size above maximum | HTTP 400 | Check maximum is enforced |
| Valid filter with no matches | HTTP 200 | Assert empty items and consistent totals |
| Unknown resource ID | HTTP 404 | Ensure no stack trace leaks |
| Unexpected server exception | HTTP 500 | Assert generic message and request ID |

## Review principles

- Test observable HTTP behavior at the MVC boundary, not only helper methods.
- Keep response messages stable and free of framework internals.
- Preserve request correlation IDs when available.
- Verify pagination validation occurs before expensive repository work.
- Run the full Maven verification command after focused tests pass.
