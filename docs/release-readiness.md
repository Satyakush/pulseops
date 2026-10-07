# Release Readiness

Before treating a PulseOps change as release-ready:

1. Run the Maven test suite.
2. Confirm API validation still returns the documented error shape.
3. Confirm `X-Request-Id` is preserved or generated.
4. Check actuator health and cache health.
5. Verify role boundaries for viewer, operator, and admin.
6. Review changed configuration for accidental secrets.
7. Update API documentation when a public contract changes.
