# Security Checklist

Before shipping an API change:

- Verify the endpoint's authentication requirement.
- Verify the minimum role required for the operation.
- Validate all externally supplied input.
- Avoid returning internal exception details.
- Keep secrets out of source control.
- Preserve stateless session behavior.
- Review newly exposed actuator or diagnostic endpoints.
- Add regression coverage for security-sensitive changes.

Security is part of the endpoint contract, not a post-deployment concern.
