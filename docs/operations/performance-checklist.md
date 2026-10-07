# Performance Checklist

Performance changes should be evaluated against the complete request path.

- Avoid unnecessary database reads.
- Use cache only where repeated reads justify it.
- Invalidate stale entries after mutations.
- Keep controller methods thin.
- Avoid blocking work inside request handlers when an asynchronous mechanism is already available.
- Measure before and after a performance change.
- Preserve correctness when optimizing latency.

A faster endpoint that returns stale or inconsistent data is not a successful optimization.
