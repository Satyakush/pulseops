package com.satyakush.pulseops.auth;

import java.util.EnumSet;
import java.util.Set;

/** Maps application roles to the explicit authorities enforced by HTTP security. */
public final class RolePermissions {
    private RolePermissions() {
    }

    public static Set<Permission> forRole(AuthRole role) {
        return switch (role) {
            case ADMIN -> Set.copyOf(EnumSet.allOf(Permission.class));
            case OPERATOR -> Set.of(
                    Permission.SERVICE_READ,
                    Permission.SERVICE_WRITE,
                    Permission.INCIDENT_READ,
                    Permission.INCIDENT_WRITE);
            case VIEWER -> Set.of(
                    Permission.SERVICE_READ,
                    Permission.INCIDENT_READ);
        };
    }

    public static boolean canWrite(AuthRole role) {
        return forRole(role).contains(Permission.SERVICE_WRITE)
                && forRole(role).contains(Permission.INCIDENT_WRITE);
    }
}
