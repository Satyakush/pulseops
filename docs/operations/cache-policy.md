# Cache Policy

PulseOps uses caching for read-heavy service and incident lookups.

## Rules

- Reads may use the configured cache when a value is available.
- Mutations must invalidate affected entries.
- Cache failures must not turn a healthy database into an unavailable API.
- TTL values are configuration-driven rather than hard-coded in business methods.
- Cache behavior should be covered by focused tests and observable through cache metrics.

The cache is an optimization layer, not the source of truth.
