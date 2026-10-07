# Service Lifecycle

A service resource represents an operationally managed component.

The lifecycle should distinguish creation from later status changes. Creation establishes the resource identity and metadata; status updates represent operational state transitions.

All mutation endpoints require operator-level or administrator-level access, while read operations can be exposed to viewer-level users according to the security policy.
