# Deployment Checklist

Before deploying PulseOps:

- Confirm the intended Spring profile is active.
- Verify no development credentials are packaged into the deployment.
- Run the automated test suite.
- Confirm the health endpoint responds successfully.
- Confirm request ids appear in API errors.
- Review cache configuration and TTLs.
- Review security route changes.
- Verify the application version and release notes.
- Keep a rollback target available.

A deployment is ready only when functional, operational, and security checks are all green.
