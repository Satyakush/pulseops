# PulseOps Architecture Overview

PulseOps is organized as a layered Spring Boot service: HTTP controllers handle transport concerns, application services own business operations, repositories isolate persistence, and infrastructure components provide caching, security, observability, and event delivery.

## Request path

1. Client sends an HTTP request.
2. Security authenticates the configured user and evaluates the route role.
3. The request-id filter establishes or preserves `X-Request-Id`.
4. The controller validates input and delegates to the application service.
5. The service performs business validation, persistence, cache operations, and event publication.
6. Exceptions are converted into the shared API error contract.
7. The response includes the request correlation identifier.

This separation keeps transport, business, and infrastructure concerns independently testable.
