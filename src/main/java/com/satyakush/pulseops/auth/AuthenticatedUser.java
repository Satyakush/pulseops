package com.satyakush.pulseops.auth;

import java.util.UUID;

public record AuthenticatedUser(UUID id, String username, AuthRole role) { }
