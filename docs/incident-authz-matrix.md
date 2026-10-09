# API authorization matrix

| Resource | HTTP method | Required authority |
| --- | --- | --- |
| Services | GET | SERVICE_READ |
| Services | POST | SERVICE_WRITE |
| Services | PATCH | SERVICE_WRITE |
| Services | DELETE | ADMIN role |
| Incidents | GET | INCIDENT_READ |
| Incidents | POST | INCIDENT_WRITE |
| Incidents | PATCH | INCIDENT_WRITE |

Registration and login are public. The actuator health endpoint is public for platform probes. All other unmatched routes require authentication. A VIEWER receives read authorities only; an OPERATOR receives read/write authorities for services and incidents. ADMIN receives the full defined permission set and the ADMIN role authority.

The current user/session stores are in-memory. This matrix describes the intended application rules, but integration tests against the Spring security filter chain remain a release gate.
