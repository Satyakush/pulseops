# Testing Matrix

PulseOps testing is organized by responsibility.

| Area | Preferred test |
| --- | --- |
| Validation | Request/constraint tests |
| Error handling | Global exception handler tests |
| Security | Route authorization tests |
| Correlation | Request-id filter tests |
| Health | Typed response contract tests |
| Cache | Cache hit/invalidation tests |
| Service behavior | Application service tests |
| Persistence | Repository/integration tests |

The goal is fast feedback for isolated behavior and a smaller number of integration tests for framework boundaries.
