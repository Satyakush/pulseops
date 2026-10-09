package com.satyakush.pulseops.auth;

/** Roles assigned to PulseOps accounts. */
public enum AuthRole {
    /** Full access to every defined permission. */ ADMIN,
    /** Read and write access to service and incident operations. */ OPERATOR,
    /** Read-only access to service and incident operations. */ VIEWER
}
