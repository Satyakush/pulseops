package com.satyakush.pulseops.auth;

import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class RolePermissionsTest { @Test void viewerIsReadOnly(){var permissions=RolePermissions.forRole(AuthRole.VIEWER); assertEquals(Set.of(Permission.SERVICE_READ,Permission.INCIDENT_READ),permissions); assertFalse(RolePermissions.canWrite(AuthRole.VIEWER));} @Test void operatorCanWriteButAdminHasAllPermissions(){assertTrue(RolePermissions.canWrite(AuthRole.OPERATOR)); assertEquals(Set.of(Permission.values()),RolePermissions.forRole(AuthRole.ADMIN));} }
