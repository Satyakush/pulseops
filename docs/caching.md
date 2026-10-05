# PulseOps caching

## Policy

PulseOps uses Spring's cache abstraction with Redis as an optional production cache.

| Area | Cached reads | Mutation policy | Default TTL |
|---|---|---|---|
| Services | list, status filter, summary, point lookup | evict all service entries | 60 seconds |
| Incidents | list, lifecycle filter, summary, point lookup | evict all incident entries | 60 seconds |

## Local development

Redis is disabled by default:

`pulseops.cache.redis.enabled: false`

This keeps H2/local development independent of external infrastructure.

## Production

Enable Redis explicitly when a Redis service is available. The cache TTL is configurable through:

`pulseops.cache.ttl: 60s`

Cached values use JSON serialization and null values are not cached.

## Observability

Cache invalidations are exposed through:

- `GET /api/v1/cache/metrics`
- the cache health indicator included in Actuator health information

Writes intentionally invalidate the complete domain cache. This favors correctness and predictable freshness over maximizing cache hit rate.
